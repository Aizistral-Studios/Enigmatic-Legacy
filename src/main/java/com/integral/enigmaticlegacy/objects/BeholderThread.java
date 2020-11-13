package com.integral.enigmaticlegacy.objects;

import static java.nio.file.StandardWatchEventKinds.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.concurrent.locks.LockSupport;

import com.integral.enigmaticlegacy.EnigmaticLegacy;

import net.minecraftforge.fml.loading.FMLPaths;

public class BeholderThread extends Thread {
	private final ClassLoader realClassLoader;
	private final WatchKey key;
	private long lastCall;

	// TODO Finish implementation and make this a separate mod

	public static void init() {
		try {
			WatchService service = FileSystems.getDefault().newWatchService();
			Path beheldPath = new File(FMLPaths.GAMEDIR.get().toFile().getCanonicalFile(), "..\\src\\main\\resources").toPath();
			WatchKey key = beheldPath
					.register(service, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_DELETE, StandardWatchEventKinds.ENTRY_MODIFY);

			System.out.println("BEHELD PATH: " + beheldPath);

			new BeholderThread(key, Thread.currentThread().getContextClassLoader()).start();
			//GradleBeholder beholder = new GradleBeholder(Thread.currentThread().getContextClassLoader());
			//FileWatcher.defaultInstance().addWatch(, beholder);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private BeholderThread(WatchKey key, ClassLoader loader) {
		this.key = key;
		this.realClassLoader = loader;
		this.lastCall = System.currentTimeMillis();
		this.setName("GRADLE-BEHOLDER");
		this.setDaemon(true);
	}

	@Override
	public void run() {
		System.out.println("I AM ALIVE");

		while (true) {

			if (System.currentTimeMillis() - this.lastCall >= 200) {
				for (WatchEvent<?> event: this.key.pollEvents()) {
					WatchEvent.Kind<?> kind = event.kind();

					if (kind == OVERFLOW) {
						continue;
					}

					WatchEvent<Path> ev = (WatchEvent<Path>)event;
					Path filename = ev.context();

					System.out.println("RECEIVED EVENT FOR FILE: " + filename + ", EVENT: " + kind.name());

					System.out.println("Upating gradle resources... ");
					try {
						Process proc = Runtime.getRuntime().exec("cmd /c cd .. && gradlew updateResources");
						proc.waitFor();
						this.printResults(proc);
						proc.destroy();
					} catch (Exception e) {
						e.printStackTrace();
					}
					System.out.println("GRADLE RESOURCES UPDATED!");

					break;
				}

				this.lastCall = System.currentTimeMillis();
				boolean valid = this.key.reset();
				this.key.pollEvents();


				if (!valid) {
					System.out.println("KEY INVALIDATED; EXITING BEHOLDER THREAD");
					break;
				}
			}

			LockSupport.parkNanos(10000);
		}
	}

	public void printResults(Process process) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
		String line = "";
		while ((line = reader.readLine()) != null) {
			EnigmaticLegacy.logger.info(line);
		}
	}
}

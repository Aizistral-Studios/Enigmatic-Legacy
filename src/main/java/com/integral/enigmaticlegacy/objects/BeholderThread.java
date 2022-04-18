package com.integral.enigmaticlegacy.objects;

import static java.nio.file.StandardWatchEventKinds.*;
import static java.nio.file.StandardCopyOption.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.LockSupport;
import java.util.stream.Stream;

import org.codehaus.plexus.util.FileUtils;

import com.integral.enigmaticlegacy.EnigmaticLegacy;

import net.minecraftforge.fml.loading.FMLPaths;
/*
public class BeholderThread extends Thread {
	public static Path beheldPath;
	public static Path copyPathGradle;
	public static Path copyPathEclipse;
	private final ClassLoader realClassLoader;
	private final WatchKey key;
	private long lastCall;

	public static void init() {
		try {
			WatchService service = FileSystems.getDefault().newWatchService();
			beheldPath = new File(FMLPaths.GAMEDIR.get().toFile().getCanonicalFile(), "..\\src\\main\\resources").toPath();
			copyPathGradle = new File(FMLPaths.GAMEDIR.get().toFile().getCanonicalFile(), "..\\build\\resources\\main").toPath();
			copyPathEclipse = new File(FMLPaths.GAMEDIR.get().toFile().getCanonicalFile(), "..\\bin\\main").toPath();
			WatchKey key = beheldPath
					.register(service, new WatchEvent.Kind[] {ENTRY_MODIFY, ENTRY_CREATE, ENTRY_DELETE}, FILE_TREE);

			EnigmaticLegacy.logger.info("BEHELD PATH: " + beheldPath);

			new BeholderThread(key, Thread.currentThread().getContextClassLoader()).start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private BeholderThread(WatchKey key, ClassLoader loader) {
		this.key = key;
		this.realClassLoader = loader;
		this.lastCall = System.currentTimeMillis();
		this.setName("BEHOLDER-THREAD");
		this.setDaemon(true);

		// Control Eclipse
		if (true) {
			for (File subFile : copyPathEclipse.toFile().listFiles()) {
				if (!subFile.isDirectory() || (subFile.getName().equals("assets") || subFile.getName().equals("data"))) {
					try {
						FileUtils.forceDelete(subFile);
					} catch (IOException ex) {
						ex.printStackTrace();
					}
				}
			}

			try {
				copyFolder(beheldPath, copyPathEclipse);
			} catch (Exception e) {
				e.printStackTrace();
			}

			EnigmaticLegacy.logger.info("Updated Eclipse resource files!");
		}
	}

	@Override
	public void run() {
		EnigmaticLegacy.logger.info("I AM ALIVE");

		while (true) {

			if (true) {
				List<Path> processedFiles = new ArrayList<>();
				boolean controlGradle = true;
				boolean controlEclipse = true;

				List<WatchEvent<?>> events = new ArrayList<>();
				events.addAll(this.key.pollEvents());
				this.key.reset();
				LockSupport.parkNanos(100000);
				events.addAll(this.key.pollEvents());

				for (WatchEvent<?> event: events) {
					WatchEvent.Kind<?> kind = event.kind();
					WatchEvent<Path> ev = (WatchEvent<Path>)event;
					Path filename = ev.context();

					try {
						if (kind == OVERFLOW) {
							continue;
						} else if (filename.toString().endsWith(".TMP")) {
							continue;
						} else if (filename.toString().endsWith(".pdnSave")) {

							if (kind == ENTRY_DELETE) {
								continue;
							}

							LockSupport.parkNanos(20000);

							String name = filename.toString();
							name = name.substring(0, name.length()-10);
							filename = new File(name).toPath();
						}

						EnigmaticLegacy.logger.info("RECEIVED EVENT FOR FILE: " + filename + ", EVENT: " + kind.name());

						if (kind == ENTRY_DELETE) {

							for (Path path : processedFiles)
								if (path.toString().equals(filename.toString())) {
									continue;
								}

							if (controlGradle) {
								FileUtils.forceDelete(new File(copyPathGradle.toFile(), filename.toString()));
							} if (controlEclipse) {
								FileUtils.forceDelete(new File(copyPathEclipse.toFile(), filename.toString()));
							}

							processedFiles.add(filename);

						} else if (kind == ENTRY_CREATE || kind == ENTRY_MODIFY) {

							for (Path path : processedFiles)
								if (path.toString().equals(filename.toString())) {
									continue;
								}

							if (controlGradle) {
								File newFile = new File(copyPathGradle.toFile(), filename.toString());
								File oldFile = new File(beheldPath.toFile(), filename.toString());

								EnigmaticLegacy.logger.info("Upating Gradle resources... ");
								EnigmaticLegacy.logger.info("Copying " + oldFile + " to " + newFile);

								mkdirsFor(newFile);
								try {
									Files.copy(oldFile.toPath(), newFile.toPath(), REPLACE_EXISTING);
								} catch (Exception ex) {
									// NO-OP
								}
							} if (controlEclipse) {
								File newFile = new File(copyPathEclipse.toFile(), filename.toString());
								File oldFile = new File(beheldPath.toFile(), filename.toString());

								EnigmaticLegacy.logger.info("Upating Eclipse resources... ");
								EnigmaticLegacy.logger.info("Copying " + oldFile + " to " + newFile);

								mkdirsFor(newFile);
								try {
									Files.copy(oldFile.toPath(), newFile.toPath(), REPLACE_EXISTING);
								} catch (Exception ex) {
									// NO-OP
								}
							}

							processedFiles.add(filename);
						}

					} catch (Exception e) {
						e.printStackTrace();
					}


					EnigmaticLegacy.logger.info("BUILD RESOURCES UPDATED!");

					break;
				}

				processedFiles.clear();
				this.key.reset();
				this.key.pollEvents();

				boolean valid = this.key.reset();

				if (!valid) {
					EnigmaticLegacy.logger.info("KEY INVALIDATED; EXITING BEHOLDER THREAD");
					break;
				}
			}

			LockSupport.parkNanos(100000);
		}
	}

	public void printResults(Process process) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
		String line = "";
		while ((line = reader.readLine()) != null) {
			EnigmaticLegacy.logger.info(line);
		}
	}

	private static void mkdirsFor(File destination) {
		// does destination directory exist ?
		try {
			File parentFile = destination.getParentFile();
			if (parentFile != null && !parentFile.exists()) {
				parentFile.mkdirs();
			}
		} catch (Exception ex) {
			// NO-OP
		}
	}

	public static void copyFolder(Path src, Path dest) {
		try {
			Files.walk(src).forEach(s -> {
				try {
					Path d = dest.resolve(src.relativize(s));
					if (Files.isDirectory(s)) {
						if (!Files.exists(d)) {
							Files.createDirectory(d);
						}
						return;
					}
					Files.copy(s, d); // use flag to override existing
				} catch (Exception ex) {
					if (!(ex instanceof FileAlreadyExistsException)) {
						ex.printStackTrace();
					} else {
						EnigmaticLegacy.logger.debug("Could not overwrite already existing file for Eclipse build: " + s.toFile().getName());
					}
				}
			});
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
 */

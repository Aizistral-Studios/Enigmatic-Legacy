rmdir build\resources\main /S /Q
mkdir build\resources\main
xcopy /E /H /Y src\main\resources build\resources\main
rmdir build\resources\test /S /Q
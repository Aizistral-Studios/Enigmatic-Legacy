rmdir bin\main\assets /S /Q
rmdir bin\main\data /S /Q

mkdir bin\main\assets
mkdir bin\main\data

xcopy /E /H /Y src\main\resources\assets bin\main\assets
xcopy /E /H /Y src\main\resources\data bin\main\data
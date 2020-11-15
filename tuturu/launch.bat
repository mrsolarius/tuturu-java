@echo off
setlocal

for /f "tokens=3" %%g in ('java -version 2^>^&1 ^| findstr /i "version"') do (
    @echo Output: %%g
    set JAVAVER=%%g
)
set JAVAVER=%JAVAVER:"=%

for /f "delims=. tokens=1-3" %%v in ("%JAVAVER%") do (
	echo %%v
    if %%v GEQ 11 (
		@echo Votre version de java est à jour
		java -Dfile.encoding=UTF-8 -jar tuturu-1.2.jar
	)else (
		@echo Imposible de lancer l'application avec Java v%JAVAVER%.
		@echo Veuillez installer une version ulterieur a Java 11.
	)
)

endlocal
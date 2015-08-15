# Инструкция #

Это руководство основано на [инструкции](http://www.the-ebook.org/forum/viewtopic.php?p=535999#535999) пользователя KKKindle с [форума the-ebook.org](http://www.the-ebook.org/forum/), за которую ему большое спасибо!

  * Скачиваем файлы [kSnake\_1.0.azw2](http://snake-game-kindlet.googlecode.com/files/kSnake_1.0.azw2) и [developer.keystore](http://snake-game-kindlet.googlecode.com/files/developer.keystore).
  * Подключаем Kindle (с выключенным usbNetwork) к компьютеру и копируем оба скачанных файла в папку documents.
  * Переводим Kindle в режим usbNetwork (подробнее [здесь](http://www.the-ebook.org/forum/viewtopic.php?p=530160#530160))
  * Командой `cd /mnt/us/documents` переходим в папку documents (которая находится в папке `us`, которая, в свою очередь, находится в папке `mnt`, которая уже находится в корневой директории /), в которой лежат нужные нам файлы.
  * Можем посмотреть список файлов и директорий в папке в которой мы находимся. Команда `ls -lh` выдаст список ваших книжек, коллекций, а также файлы, которые мы скопировали на устройство:
```
-rwxr-xr-x    1 root     root         3.6k Dec  2  2010 developer.keystore
-rwxr-xr-x    1 root     root        19.2k Dec  2  2010 kSnake_1.0.azw2
```
  * Команда `mv ./developer.keystore /var/local/java/keystore/` перенесёт файл с ключами разработчиков `developer.keystore` (необходимый для запуска киндлета snake-game) из текущей папки в папку `/var/local/java/keystore/`.
  * Перезапускаем Kindle
  * Наслаждаемся игрой :)
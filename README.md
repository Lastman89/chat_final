# Алгоритм работы


**Алгоритм работы сервера**

1. Запуск сервера и настройка конфигурация сервера
2. Сервер должен уметь работать с несколькими клиентами одновеременно в соответствии с количеством потоков определнных для асинхронного общения с клиентами
3. Выгрузка конфигураций сервера для подключения клиентов:
    * Сериализация конфигураций в формат JSON
    * Создание файла с конфигурациями в формате .JSON в директории \files\
4. Создать потоки для множественного подключения клиентов
5. Ожидание подключения клиентов
6. Проверить успешность подключения клиента:
    * Если подключение клиента успешно, то:
        + Отправить сообщение клиенту с просьбой ввести никнэйм клиента
        + Отправить приветственное сообщение клиенту
        + Перейти на шаг 6
    * Иначе:
        + Инициировать исключение
        + Отправить клиенту
        + Перейти на шаг 4
7. Ожидание сообщения клиента\
    * Проверть сообщение клиента
        + Если сообщение "exit", то: 
            - Завершить подключение клиента
            - Перейти на шаг 4
        + Иначе: 
            - Отправить сообщение всем клиентам
            - Залогировать сообщение со следующими параметрами:
                + Ник клиента + время логирования + содержание сообщения
            - Логирование осуществлять в файле с расширением .log
            - Файл расположить в диретории \files\serverLogs\
            - Перейти на шаг 6
            
**Алгоритм работы клиента**

1. Десериализовать конфиги для подключения из директории \files\
2. Подключиться к серверу по конфигурациям полученным на шаге 1
3. Слушать ответ сервера:
    * Если ответа с успешным подключением нет, то: 
        + Инициализировать ошибку подключения к серверу
        + Завершить алгоритм
    * Иначе обработать ответ сервера, вывести на экран
4. Обеспечить возможность асинхронного получения всех сообщений от сервера и отправку сообщений на сервер
5. Обеспечить возможность введения сообщения пользователем с консоли
    * Логировать каждое сообщение пользователя со следующими параметрами:
        + Время ввода сообщения
        + Никнэйм пользователя
        + Соддержание сообщения
    * Файл с логами клиента расположить в диретории \files\clientLogs\
    * Логирование осуществлять в разрезе каждого пользователя
    * Если файл с логами для клиента уже создан, то осуществлять логирование в существующем файлу без потери ранее записанных логов
    * Иначе создать файл с именем: никнэйм + .log
    * Отправить сообщение на сервер
        + Если сообщение "exit", то:
           - Отправить на сервер
           -  Завершить работу клиента
    * Иначе продолжить диалог с сервером

**Верхнеуровневая диаграмма активности для проекта "Сетевой чат"**

![diagramm](/img/Диаграмма%20Клиент-Сервер.png) 

**Тестирование с помощью telnet**

Шаг 1. Пробуем подключиться к серверу через telnet\
![connect](/img/1.PNG)

Шаг 2. Ответ сервера\
![answer](/img/2.PNG)

Шаг 3. Пишем сообщение серверу и получаем ответ
![dialog](/img/3.PNG)

Шаг 4. Пишем в чат
![chat](/img/4.PNG)
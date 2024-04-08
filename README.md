# Алгоритм работы


**Алгоритм работы сервера**

1. Запуск сервера и настройка конфигурация сервера
2. Сервер должен уметь работать с несколькими клиентами одновеременно в соответствии с количеством потоков определнных для асинхронного общения с клиентами
3. Выгрузка конфигураций сервера для подключения клиентов:
    * Сериализация конфигураций в формат JSON\
    * Создание файла с конфигурациями в формате .JSON в директории \files\
4. Создать потоки для множественного подключения клиентов
5. Ожидание подключения клиентов
6. Проверить успешность подключения клиента:
    * Если подключение клиента успешно, то:
        + Отправить сообщение клиенту с просьбой ввести никнэйм клиента
        + Отправить приветственное сообщение клиенту
        + Перейти на шаг 6
    * Иначе:\
        + Инициировать исключение
        + Отправить клиенту
        + Перейти на шаг 4
7. Ожидание сообщения клиента\
    * Проверть сообщение клиента
        + Если сообщение "exit", то: 
            - завершить подключение клиента
            - перейти на шаг 4
        + Иначе: 
            - отправить сообщение всем клиентам
            - залогировать сообщение со следующими параметрами:
                + Ник клиента + время логирования + содержание сообщения
            - логирование осуществлять в файле с расширением .log
            - файл расположить в диретории \files\serverLogs\
            - начать шаг 6 сначала
**Алгоритм работы клиента



![diagramm](/img/Диаграмма%20Клиент-Сервер.png) 
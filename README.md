# exchange-rates

Exchange rates aggregator service

[HEROKU](https://quiet-gorge-67795.herokuapp.com)
[SWAGGER](https://quiet-gorge-67795.herokuapp.com/swagger-ui.html#/)

Загрузить файл в систему (с помощью RESTful API)

```
**POST** /api/v1/upload
```

* [CSV BANK.csv](https://quiet-gorge-67795.herokuapp.com/CSV%20BANK.CSV)
* [XML BANK.xml](https://quiet-gorge-67795.herokuapp.com/CSV%20BANK.XML)
* [JSON BANK.json](https://quiet-gorge-67795.herokuapp.com/CSV%20BANK.JSON)

Курс покупки указанной валюты всеми банками. 
Есть возможность сортировки результатов по убыванию/возрастанию.

```
GET /api/v1/exchangerates/{CURRENCY}/buy?orderBy={field},{type}
GET /api/v1/exchangerates?currency={CURRENCY}&bank={BANK}
```

Курс продажи указанной валюты всеми банками. 
Есть возможность сортировки результатов по убыванию/возрастанию.

```
GET /api/v1/exchangerates/{CURRENCY}/sell?orderBy={field},{type}
GET /api/v1/exchangerates?currency={CURRENCY}&bank={BANK}
```

Обновить курс покупки или продажи указанной валюты для отдельно взятого банка. 

```
PUT /api/v1/exchangerates/{CURRENCY}/sell/{BANK}
PUT /api/v1/exchangerates/{CURRENCY}/sell/{BANK}
```

Установить/снять запрет на покупку или продажу определенной валюты.

```
DELETE /api/v1/exchangerates/{CURRENCY}/buy/{BANK}
DELETE /api/v1/exchangerates/{CURRENCY}/sell/{BANK}
```

Удалить все предложения по покупке/продаже валюты для
определенного банка.

```
DELETE /exchangerates/buy/{BANK}
DELETE /exchangerates/sell/{BANK}
```

Создавать отчет с наилучшими предложениями по покупке и продаже
всех валют, который в какой-то форме содержит следующую
информацию

```
GET /exchangerates/report
```

```
[
    {
        "currency": "EUR",
        "buyRate": 33,
        "buyBank": "CSV BANK",
        "sellRate": 30,
        "sellBank": "XML BANK"
    },
    {
        "currency": "USD",
        "buyRate": 28,
        "buyBank": "JSON BANK",
        "sellRate": 10,
        "sellBank": "DevBootstrap"
    },
    {
        "currency": "UAH",
        "buyRate": 1.1,
        "buyBank": "DevBootstrap2",
        "sellRate": 1,
        "sellBank": "DevBootstrap"
    }
]
```
INSERT INTO transaction(uuid, description, status, amount, currency, date_created, merchant_uuid, merchant_name)
values('022af304-6d66-4980-ae94-18804977f5e3', 'Transaction 1', 'CREATED', '612.89', 'GBP', {ts '2019-09-17 18:47:52.69'}, '54910338-7d12-4f96-9832-6013c4aaa142', 'Albert Einstein');

INSERT INTO transaction(uuid, description, status, amount, currency, date_created, merchant_uuid, merchant_name)
values('dd0f4b00-7bbd-4ee3-8360-d17fd57b653a', 'Transaction 2', 'CREATED', '150', 'GBP', {ts '2019-09-21 14:01:50.00'}, '54910338-7d12-4f96-9832-6013c4aaa142', 'Albert Einstein');

INSERT INTO transaction(uuid, description, status, amount, currency, date_created, merchant_uuid, merchant_name)
values('efe97b06-1ab8-415a-9418-e422830abf21', 'Transaction 3', 'CREATED', '1000.04', 'GBP', {ts '2021-02-11 08:01:00.00'}, '54910338-7d12-4f96-9832-6013c4aaa142', 'Albert Einstein');

INSERT INTO transaction(uuid, description, status, amount, currency, date_created, merchant_uuid, merchant_name)
values('22613e22-0276-47a5-ae24-02e33d269b21', 'Transaction 4', 'CREATED', '8590.87', 'GBP', {ts '2021-02-23 12:45:05.00'}, '54910338-7d12-4f96-9832-6013c4aaa142', 'Albert Einstein');

INSERT INTO transaction(uuid, description, status, amount, currency, date_created, merchant_uuid, merchant_name)
values('9826868c-a9c8-415a-9b51-2a6f2c76f28a', 'Transaction 5', 'CREATED', '567.51', 'USD', {ts '2020-11-20 16:45:41.14'}, 'daccd4ef-e34c-4494-bece-292eafdc2786', 'Ada Lovelace');

INSERT INTO transaction(uuid, description, status, amount, currency, date_created, merchant_uuid, merchant_name)
values('cef42378-953c-495c-98ca-6dba4e70deda', 'Last one', 'REFUNDED', '300.76', 'USD', {ts '2021-08-11 09:30:11.11'}, 'daccd4ef-e34c-4494-bece-292eafdc2786', 'Ada Lovelace');
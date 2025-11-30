-- Пользователь admin, пароль "admin"
-- md5(admin) = 21232f297a57a5a743894a0e4a801fc3

INSERT INTO users (name, login, password, is_work)
VALUES ('Иванов Иван Иванович', 'admin', '21232f297a57a5a743894a0e4a801fc3', TRUE)
ON CONFLICT (login) DO NOTHING;


-- Типы активов

INSERT INTO active_types (name, is_soft, number_template)
VALUES
    ('Монитор', FALSE, 'MON-{yyyy}-{seq}'),
    ('Материнская плата', FALSE, 'MB-{yyyy}-{seq}'),
    ('ПО', TRUE, 'SW-{yyyy}-{seq}')
ON CONFLICT (name) DO NOTHING;


-- Статусы (отдельно для железа и софта по флагу is_soft)

INSERT INTO statuses (name, is_soft) VALUES
    ('На складе', FALSE),
    ('Используется', FALSE),
    ('На складе', TRUE),
    ('Используется', TRUE);


-- Пара активов (железо)

-- Монитор
INSERT INTO actives (name, typeid, description, parentactiveid,
                     uniquenumber, statusid, serialnumber,
                     bitrixcode, onescode, address)
SELECT
    'Монитор Samsung 24"'      AS name,
    t.id                       AS typeid,
    'Рабочий монитор'          AS description,
    NULL                       AS parentactiveid,
    'INV-0001'                 AS uniquenumber,
    s.id                       AS statusid,
    'SN-MON-001'               AS serialnumber,
    'B123'                     AS bitrixcode,
    '1C-MON-001'               AS onescode,
    'Офис, кабинет 101'        AS address
FROM active_types t, statuses s
WHERE t.name = 'Монитор'
  AND s.name = 'Используется'
  AND s.is_soft = FALSE
LIMIT 1;


-- Материнская плата
INSERT INTO actives (name, typeid, description, parentactiveid,
                     uniquenumber, statusid, serialnumber,
                     bitrixcode, onescode, address)
SELECT
    'Материнская плата ASUS'   AS name,
    t.id                       AS typeid,
    'Запасная плата'           AS description,
    NULL                       AS parentactiveid,
    'INV-0002'                 AS uniquenumber,
    s.id                       AS statusid,
    'SN-MB-001'                AS serialnumber,
    'B124'                     AS bitrixcode,
    '1C-MB-001'                AS onescode,
    'Склад, стеллаж №3'        AS address
FROM active_types t, statuses s
WHERE t.name = 'Материнская плата'
  AND s.name = 'На складе'
  AND s.is_soft = FALSE
LIMIT 1;


-- Назначим оба актива пользователю admin

INSERT INTO users_actives (activeid, userid, isapproved)
SELECT a.id, u.id, TRUE
FROM actives a
JOIN users u ON u.login = 'admin'
ON CONFLICT DO NOTHING;


-- Пара комментариев

INSERT INTO comments (activeid, comment, isprivat)
SELECT a.id, 'Проверен 01.01.2025', FALSE
FROM actives a
WHERE a.uniquenumber = 'INV-0001';

INSERT INTO comments (activeid, comment, isprivat)
SELECT a.id, 'Не выдавать без согласования с ИТ', TRUE
FROM actives a
WHERE a.uniquenumber = 'INV-0002';


-- Пара доп. атрибутов

INSERT INTO active_attributes (name, activeid, typeid)
SELECT 'Диагональ: 24"', a.id, t.id
FROM actives a
JOIN active_types t ON t.name = 'Монитор'
WHERE a.uniquenumber = 'INV-0001';

INSERT INTO active_attributes (name, activeid, typeid)
SELECT 'Чипсет: B550', a.id, t.id
FROM actives a
JOIN active_types t ON t.name = 'Материнская плата'
WHERE a.uniquenumber = 'INV-0002';

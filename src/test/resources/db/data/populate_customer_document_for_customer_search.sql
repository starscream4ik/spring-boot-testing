INSERT INTO document
      (id, category, upload_time, customer_id, status, update_time, upload_bank)
VALUES
       (201,  'BANK_STATEMENT', now(), 1235, 'DELETED', now(), 'aval'),
       (202,  'BANK_STATEMENT', now(), 1235, 'DELETED', now() , 'privat'),
       (203,  'BANK_STATEMENT', now(), 1236, 'CREATED', now(), 'aval'),
       (204,  'BANK_STATEMENT', now(), 1236, 'ACCEPTED', now(), 'privat'),
       (205,  'BANK_STATEMENT', now(), 1237, 'CREATED', now(), 'aval'),
       (206,  'BANK_STATEMENT', now(), 1237, 'CREATED', now(), 'privat');
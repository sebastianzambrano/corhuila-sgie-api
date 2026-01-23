-- Crear usuario de aplicación para DEV
DO $$
BEGIN
   IF NOT EXISTS (SELECT FROM pg_roles WHERE rolname = 'expense_user_dev') THEN
      CREATE USER expense_user_dev WITH PASSWORD 'dev123456';
   END IF;

   IF NOT EXISTS (SELECT FROM pg_roles WHERE rolname = 'sebastianzambrano') THEN
      CREATE USER sebastianzambrano WITH PASSWORD 'dev123456';
   END IF;
END
$$;

-- Permisos
GRANT ALL PRIVILEGES ON DATABASE sgie_db TO expense_user_dev;
GRANT ALL PRIVILEGES ON DATABASE sgie_db TO sebastianzambrano;

GRANT ALL PRIVILEGES ON SCHEMA public TO expense_user_dev;
GRANT ALL PRIVILEGES ON SCHEMA public TO sebastianzambrano;

ALTER DEFAULT PRIVILEGES IN SCHEMA public
GRANT ALL ON TABLES TO expense_user_dev;

ALTER DEFAULT PRIVILEGES IN SCHEMA public
GRANT ALL ON SEQUENCES TO expense_user_dev;
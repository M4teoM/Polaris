-- Elimina la relación de operario en reservas
ALTER TABLE IF EXISTS reserva_habitacion
    DROP COLUMN IF EXISTS id_operario;
ALTER TABLE cuenta ALTER COLUMN id_reserva SET NULL;
ALTER TABLE item_cuenta ADD COLUMN IF NOT EXISTS pagado BOOLEAN DEFAULT FALSE NOT NULL;
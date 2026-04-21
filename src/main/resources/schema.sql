-- Elimina la relación de operario en reservas
ALTER TABLE IF EXISTS reserva_habitacion
    DROP COLUMN IF EXISTS id_operario;

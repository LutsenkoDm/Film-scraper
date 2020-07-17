create function get_history(curruserid bigint) RETURNS setof integer
    language sql
as
$$
SELECT film_id FROM schedule where id IN
        (SELECT schedule_id FROM history where user_id = currUserId)
$$;

alter function get_history(bigint) owner to postgres;
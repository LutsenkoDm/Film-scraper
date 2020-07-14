create function get_history(curruserid integer) RETURNS setof integer
    language sql
as
$$
SELECT film_id FROM schedule where id IN
        (SELECT schedule_id FROM history where user_id = currUserId)
$$;

alter function get_history(integer) owner to postgres;
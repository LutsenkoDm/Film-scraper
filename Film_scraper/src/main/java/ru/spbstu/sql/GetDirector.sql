create function get_director(curruserid integer) RETURNS setof varchar
    language sql
as
$$
SELECT director FROM film where id IN
    (SELECT film_id FROM schedule where id IN
        (SELECT schedule_id FROM history where user_id = currUserId)
    );
$$;

alter function get_director(integer) owner to postgres;


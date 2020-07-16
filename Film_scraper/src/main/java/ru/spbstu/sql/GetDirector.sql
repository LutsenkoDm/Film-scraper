create function get_director(curruserid bigint) RETURNS setof varchar
    language sql
as
$$
SELECT director FROM film where id IN
    (SELECT film_id FROM schedule where id IN
        (SELECT schedule_id FROM history where user_id = currUserId)
    );
$$;

alter function get_director(bigint) owner to postgres;


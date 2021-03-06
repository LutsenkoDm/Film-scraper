create function get_genres(curruserid bigint) RETURNS setof varchar
    language sql
as
$$
SELECT genre FROM film where id IN
    (SELECT film_id FROM schedule where id IN
        (SELECT schedule_id FROM history where user_id = currUserId)
    );
$$;

alter function get_genres(bigint) owner to postgres;


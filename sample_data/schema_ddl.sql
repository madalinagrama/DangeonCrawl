DROP TABLE IF EXISTS public.game_state CASCADE ;
CREATE TABLE public.game_state (
                                   id serial NOT NULL PRIMARY KEY,
                                   saved_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
                                   player_id integer NOT NULL,
                                   discovered_maps json


);

DROP TABLE IF EXISTS public.player CASCADE ;
CREATE TABLE public.player (
                               id serial NOT NULL PRIMARY KEY,
                               player_name text NOT NULL,
                               hp integer NOT NULL,
                               damage integer NOT NULL,
                               armor integer NOT NULL,
                               x integer NOT NULL,
                               y integer NOT NULL,
                               map_id integer,
                               inventory json

);

DROP TABLE IF EXISTS public.actors CASCADE ;
CREATE TABLE public.actors (
                               id serial NOT NULL PRIMARY KEY,
                               actor_type text NOT NULL,
                               hp integer NOT NULL,
                               x integer NOT NULL,
                               y integer NOT NULL,
                               map_id integer NOT NULL
);

DROP TABLE IF EXISTS  public.maps CASCADE ;
CREATE TABLE public.maps (
                             id serial NOT NULL PRIMARY KEY,
                             name text NOT NULL ,
                             map text NOT NULL,
                             game_state_id serial NOT NULL

);
--
-- ALTER TABLE ONLY public.game_state
--     ADD CONSTRAINT fk_player_id FOREIGN KEY (player_id) REFERENCES public.player(id);
--
-- ALTER TABLE ONLY public.player
--     ADD CONSTRAINT fk_map_id FOREIGN KEY (map_id) REFERENCES public.maps(id);
--
-- ALTER TABLE ONLY public.actors
--     ADD CONSTRAINT fk_map_id FOREIGN KEY (map_id) REFERENCES public.maps(id);
--
-- ALTER TABLE ONLY public.items
--     ADD CONSTRAINT fk_map_id FOREIGN KEY (map_id) REFERENCES public.maps(id);


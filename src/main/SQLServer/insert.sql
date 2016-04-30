INSERT INTO Movie (title,release_year) VALUES ('Star Wars IV','19770525');
INSERT INTO Movie (title,release_year) VALUES ('Star Wars V','19800629');
INSERT INTO Movie (title,release_year) VALUES ('Star Wars VI','19830525');

INSERT INTO Review (movie_id,name,review,summary,rating) VALUES ('1','Luis','Muito Bom','Gostei','5');
INSERT INTO Review (movie_id,name,review,summary,rating) VALUES ('1','Red','Nao gostei','mau','1');
INSERT INTO Review (movie_id,name,review,summary,rating) VALUES ('1','Hugo','Podia ser melhor','Meh','3');

INSERT INTO Review (movie_id,name,review,summary,rating) VALUES ('2','Luis','Muito Bom','Gostei','4');
INSERT INTO Review (movie_id,name,review,summary,rating) VALUES ('2','Red','Nao gostei','mau','2');
INSERT INTO Review (movie_id,name,review,summary,rating) VALUES ('2','Hugo','Podia ser melhor','Meh','3');

INSERT INTO Rating (movie_id,one,two,three,four,five) VALUES ('1','1','2','3','4','5');
INSERT INTO Rating (movie_id,one,two,three,four,five) VALUES ('2','5','4','3','2','1');
INSERT INTO Rating (movie_id,one,two,three,four,five) VALUES ('3','3','4','1','7','11');
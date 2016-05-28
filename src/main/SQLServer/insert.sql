INSERT INTO Movie (title,release_year) VALUES ('Star Wars IV','19770525');
INSERT INTO Movie (title,release_year) VALUES ('Star Wars V','19800629');
INSERT INTO Movie (title,release_year) VALUES ('Star Wars VI','19830525');

INSERT INTO Movie (title,release_year) VALUES ('Saw I','20040101');
INSERT INTO Movie (title,release_year) VALUES ('Saw II','20050101');
INSERT INTO Movie (title,release_year) VALUES ('Saw III','20060101');
INSERT INTO Movie (title,release_year) VALUES ('Saw IV','20070101');
INSERT INTO Movie (title,release_year) VALUES ('Saw V','20080101');
INSERT INTO Movie (title,release_year) VALUES ('Saw 6','20090101');

INSERT INTO Rating (movie_id,one,two,three,four,five) VALUES ('1','1','2','3','4','5');
INSERT INTO Rating (movie_id,one,two,three,four,five) VALUES ('2','5','4','3','2','1');
INSERT INTO Rating (movie_id,one,two,three,four,five) VALUES ('3','3','4','1','7','11');

INSERT INTO Rating (movie_id,one,two,three,four,five) VALUES ('4','3','2','5','4','0');
INSERT INTO Rating (movie_id,one,two,three,four,five) VALUES ('5','3','2','3','3','0');
INSERT INTO Rating (movie_id,one,two,three,four,five) VALUES ('6','3','2','4','3','0');
INSERT INTO Rating (movie_id,one,two,three,four,five) VALUES ('7','2','1','5','3','0');
INSERT INTO Rating (movie_id,one,two,three,four,five) VALUES ('8','2','1','5','3','0');
INSERT INTO Rating (movie_id,one,two,three,four,five) VALUES ('9','2','1','3','3','1');

INSERT INTO Review (movie_id,name,review,summary,rating) VALUES ('1','Luis','Muito Bom','Gostei','5');
INSERT INTO Review (movie_id,name,review,summary,rating) VALUES ('1','Red','Nao gostei','mau','1');
INSERT INTO Review (movie_id,name,review,summary,rating) VALUES ('1','Hugo','Podia ser melhor','Meh','3');

INSERT INTO Review (movie_id,name,review,summary,rating) VALUES ('2','Luis','Muito Bom','Gostei','4');
INSERT INTO Review (movie_id,name,review,summary,rating) VALUES ('2','Red','Nao gostei','mau','2');
INSERT INTO Review (movie_id,name,review,summary,rating) VALUES ('2','Hugo','Podia ser melhor','Meh','3');

INSERT INTO Review (movie_id,name,review,summary,rating) VALUES ('4','Hugo','Podia ser melhor','Meh','4');
INSERT INTO Review (movie_id,name,review,summary,rating) VALUES ('5','Red','Nao gostei','mau','1');
INSERT INTO Review (movie_id,name,review,summary,rating) VALUES ('6','Luis','Muito Bom','Gostei','4');
INSERT INTO Review (movie_id,name,review,summary,rating) VALUES ('7','Hugo','Podia ser melhor','Meh','4');
INSERT INTO Review (movie_id,name,review,summary,rating) VALUES ('8','Red','Nao gostei','mau','1');
INSERT INTO Review (movie_id,name,review,summary,rating) VALUES ('9','Luis','Muito Bom','Gostei','4');

INSERT INTO Collection (name,description) VALUES ('CollectionOne','The very first one');
INSERT INTO Collection (name,description) VALUES ('CollectionTwo','The one that comes after');
INSERT INTO Collection (name,description) VALUES ('CollectionThree','The one that shouldnt be');

INSERT INTO Collection (name,description) VALUES ('Saw triology','the first 3');
INSERT INTO Collection (name,description) VALUES ('Saw triology','The next ones');
INSERT INTO Collection (name,description) VALUES ('Saw','All of them');

INSERT INTO Has (collection_id,movie_id) VALUES ('1','1');
INSERT INTO Has (collection_id,movie_id) VALUES ('1','2');
INSERT INTO Has (collection_id,movie_id) VALUES ('1','3');
INSERT INTO Has (collection_id,movie_id) VALUES ('2','1');
INSERT INTO Has (collection_id,movie_id) VALUES ('2','2');

INSERT INTO Has (collection_id,movie_id) VALUES ('4','4');
INSERT INTO Has (collection_id,movie_id) VALUES ('4','5');
INSERT INTO Has (collection_id,movie_id) VALUES ('4','6');
INSERT INTO Has (collection_id,movie_id) VALUES ('5','7');
INSERT INTO Has (collection_id,movie_id) VALUES ('5','8');
INSERT INTO Has (collection_id,movie_id) VALUES ('5','9');
INSERT INTO Has (collection_id,movie_id) VALUES ('6','4');
INSERT INTO Has (collection_id,movie_id) VALUES ('6','5');
INSERT INTO Has (collection_id,movie_id) VALUES ('6','6');
INSERT INTO Has (collection_id,movie_id) VALUES ('6','7');
INSERT INTO Has (collection_id,movie_id) VALUES ('6','8');
INSERT INTO Has (collection_id,movie_id) VALUES ('6','9');

CREATE TABLE Movie(
	movie_id int IDENTITY(1,1) PRIMARY KEY,
	title varchar(45) NOT NULL,
	release_year datetime NOT NULL,
	genre varchar(200),
	cast_directors varchar(45),
	cover binary NULL,
)

CREATE TABLE Review(
	review_id int IDENTITY(1,1),
	movie_id int FOREIGN KEY REFERENCES Movie(movie_id),
	name varchar(50) NOT NULL,
	review varchar(255) NOT NULL,
	summary varchar(200) NOT NULL,
	rating int NOT NULL,
	PRIMARY KEY(review_id, movie_id)
)

CREATE TABLE Rating(
	rating_id int IDENTITY(1,1),
	movie_id int FOREIGN KEY REFERENCES Movie(movie_id),
	one INT NULL,
	two INT NULL,
	three INT NULL,
	four INT NULL,
	five INT NULL,
	PRIMARY KEY(rating_id, movie_id)
)


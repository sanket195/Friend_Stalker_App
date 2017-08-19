
INSERT INTO users (  user_email,user_password,user_firstname,user_lastname )
                       VALUES
                       ( "trilok@gmail.com","abcd","trilok","bakula");

INSERT INTO users (  user_email,user_password,user_firstname,user_lastname )
                       VALUES
                       ( "sanket@gmail.com","abcd","sanket","patel");

INSERT INTO users (  user_email,user_password,user_firstname,user_lastname )
                       VALUES
                       ( "deeksha@gmail.com","abcd","deeksha","balaji");

INSERT INTO users (  user_email,user_password,user_firstname,user_lastname )
                       VALUES
                       ( "juhi@gmail.com","abcd","juhi","rajmohan");

INSERT INTO users (  user_email,user_password,user_firstname,user_lastname )
                       VALUES
                       ( "ravi@gmail.com","abcd","ravi","kanth");


INSERT INTO location (user_id,latitude,longitude)
                       VALUES
                       (1,"51.5034070","-0.1275920");

INSERT INTO location (user_id,latitude,longitude)
                       VALUES
                        (2,"22.7201320","71.6495360");

INSERT INTO location (user_id,latitude,longitude)
                       VALUES
                        (3,"23.0225050","72.5713620");

INSERT INTO friends (friends_id,user_email,friend_email, is_friend)
                       VALUES
                        (1,"sanket@gmail.com","trilok@gmail.com","true");

INSERT INTO friends (friends_id,user_email,friend_email, is_friend)
                       VALUES
                        (2,"juhi@gmail.com","deeksha@gmail.com","true");

INSERT INTO friends (friends_id,user_email,friend_email, is_friend)
                       VALUES
                        (3,"trilok.com","ravi@gmail.com","true");

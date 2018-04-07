cp -rf  src/main/resources/online/* src/main/resources/ 
mvn package
mkdir output
mv target/*.war output

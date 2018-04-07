cp -rf  src/main/resources/online/* src/main/resources/ 
mvn package -Dmaven.test.skip=true
mkdir output
mv target/*.war output

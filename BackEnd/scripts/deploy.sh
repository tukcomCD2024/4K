KEY=/home/ubuntu/key

REPOSITORY=/home/ubuntu/4k
cd $REPOSITORY

echo "> Paste Folder"
cp -r $KEY/ssl $REPOSITORY/src/main/resources
cp -r $KEY/firebase $REPOSITORY/src/main/resources

JAR_NAME=$(ls $REPOSITORY/build/libs/ | grep 'SNAPSHOT.jar' | tail -n 1)
JAR_PATH=$REPOSITORY/build/libs/$JAR_NAME

CURRENT_PID=$(pgrep -fl 'java -jar /home/ubuntu/4k/build/libs/webChat-0.0.1-SNAPSHOT.jar')

if [ -z $CURRENT_PID ]
then
  echo "> 종료할 애플리케이션이 없습니다."
else
  echo "> kill -9 $CURRENT_PID"
  kill -15 $CURRENT_PID
  sleep 5
fi

echo "> Deploy - $JAR_PATH "
nohup java -jar $JAR_PATH > $REPOSITORY/build/libs/nohup.out 2>&1 &
echo "Install common projects ..... ....."

cd ./mock-bgw
call mvn clean compile exec:java -Dexec.mainClass="com.glbpay.mock.Main"

echo "deploy finished ..... ....."

pause
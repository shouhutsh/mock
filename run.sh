echo "Install common projects ..... ....."

cd ./mock-bgw
mvn clean compile exec:java -Dexec.mainClass="com.glbpay.mock.Main"

echo "deploy finished ..... ....."
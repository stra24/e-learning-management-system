# Java21をベースイメージとする。
FROM eclipse-temurin:21-jdk-alpine

# コンテナ内の作業ディレクトリ（コマンドの実行場所）を/appに設定する。
WORKDIR /app

# ローカルにあるbuild/libs/*.jarを、コンテナ内の/app/app.jarにコピーする。
COPY build/libs/*.jar app.jar

# コンテナ起動時に、java -jar /app/app.jar というコマンドを実行する。（つまりアプリケーションが実行される）
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
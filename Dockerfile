ARG TARGET_APP=safety-management
ARG VERSION=0.0.1
ARG ENV_TYPE=dev

# ビルドステージ
FROM gradle:8.10.2-jdk21-alpine AS builder
ARG TARGET_APP
ARG VERSION
ARG ENV_TYPE
COPY --chown=gradle:gradle ./ /home/gradle/

WORKDIR /home/gradle

# system.propertiesをテスト用に修正
# backup作成
RUN cp /home/gradle/${TARGET_APP}/src/main/resources/system.properties /home/gradle/${TARGET_APP}/src/main/resources/system_${ENV_TYPE}.properties
# MQTTの設定をテスト用に変更
RUN sed -i "s/^mqtt.host.*/mqtt.host=localhost/g" /home/gradle/${TARGET_APP}/src/main/resources/system.properties
RUN sed -i "s/^mqtt.port.*/mqtt.port=1883/g" /home/gradle/${TARGET_APP}/src/main/resources/system.properties

# using test
RUN gradle :${TARGET_APP}:build --no-daemon 

# system.propertiesをbackupから復元
RUN mv /home/gradle/${TARGET_APP}/src/main/resources/system_${ENV_TYPE}.properties /home/gradle/${TARGET_APP}/src/main/resources/system.properties 

# war作成
RUN gradle :${TARGET_APP}:bootwar --no-daemon 

# 実行ステージ
FROM eclipse-temurin:21-jre-jammy
ENV DEBIAN_FRONTEND=noninteractive
RUN apt-get update && apt-get -y upgrade && rm -rf /var/lib/apt/lists/*
ARG TARGET_APP
ARG VERSION

# 設定ファイル用ディレクトリ
RUN mkdir /config
# 作業ディレクトリ
WORKDIR /app
# ビルドステージからSpring Bootアプリケーションのwarファイルをコピー
COPY --from=builder /home/gradle/${TARGET_APP}/build/libs/${TARGET_APP}-${VERSION}.war app.war
# ポートを公開 (Spring Bootのデフォルトポート)
EXPOSE 8080
EXPOSE 48080

# アプリケーションの実行コマンド
ENTRYPOINT ["java","-jar","app.war"]

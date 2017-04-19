FROM java:8

ENV GRADLE_VERSION 2.13
ENV GRADLE_URL https://services.gradle.org/distributions/gradle-${GRADLE_VERSION}-bin.zip
#ENV GRADLE_SHA1
ENV GRADLE_HOME /usr/lib/gradle-${GRADLE_VERSION}
ENV GRADLE_REF /usr/lib/gradle-ref
ENV PATH $PATH:${GRADLE_HOME}/bin

ENV GRADLE_CONFIG /root/.gradle
VOLUME $GRADLE_CONFIG

ENV COPY_REFERENCE_FILE_LOG $GRADLE_CONFIG/copy_reference_file.log


RUN cd /usr/lib && \
    curl -fsSL $GRADLE_URL -o gradle-bin.zip && \
    unzip gradle-bin.zip && \
    ln -s "/usr/lib/gradle-${GRADLE_VERSION}/bin/gradle" /usr/bin/gradle && \
    rm gradle-bin.zip && \
    mkdir -p /src $GRADLE_REF

WORKDIR /src

COPY . /src

RUN /src/gradlew test
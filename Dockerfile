FROM java:8-jdk
MAINTAINER Diogo Pinto

ENV SCALA_VERSION 2.11.7
ENV SBT_VERSION 0.13.9

# Install Scala
RUN \
  wget http://downloads.typesafe.com/scala/$SCALA_VERSION/scala-$SCALA_VERSION.tgz && tar xfz scala-$SCALA_VERSION.tgz -C /root/ && \
  echo >> /root/.bashrc && \
  echo 'export PATH=~/scala-$SCALA_VERSION/bin:$PATH' >> /root/.bashrc

# Install sbt
RUN \
  wget http://dl.bintray.com/sbt/debian/sbt-$SBT_VERSION.deb && \
  dpkg -i sbt-$SBT_VERSION.deb && \
  rm sbt-$SBT_VERSION.deb && \
  apt-get update && \
  apt-get install sbt

# Compile from source
COPY src/ /root/skywatch-cssim/src
COPY project/ /root/skywatch-cssim/project
#COPY target/ /root/skywatch-cssim/target
ADD build.sbt /root/skywatch-cssim/build.sbt

WORKDIR "/root/skywatch-cssim"

EXPOSE 8080

# Compile on runtime
RUN sbt assembly
#docker run -v ~/.ivy2:/root/.ivy2  -v ~/.sbt:/root/.sbt --name search_instance -i -p 8888:8888 -t cssim

# Run from jar
CMD ["java", "-jar", "/root/skywatch-cssim/target/scala-2.11/skywatch-cssim.jar"]

# Run commands
#   docker build -t skywatch-cssim .
#   docker run --name search_instance -i -p 8080:8080 -t skywatch-cssim
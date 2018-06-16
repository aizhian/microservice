FROM centos:centos7

RUN groupadd -r redis && useradd -r -g redis redis -p redis

ENV REDIS_VERSION 3.2.9
ENV REDIS_DOWNLOAD_URL http://download.redis.io/releases/redis-3.2.9.tar.gz
ENV REDIS_DOWNLOAD_SHA 8fad759f28bcb14b94254124d824f1f3ed7b6aa6

# for redis-sentinel see: http://redis.io/topics/sentinel
RUN set -ex && \
	\
	buildDeps=' \
		wget \
		\
		gcc \
		glibc-devel.x86_64 \
		make \
		net-tools \
		ruby ruby-devel rubygems\
	'&& \
	yum -y --setopt=tsflags=nodocs update && \
	yum -y --setopt=tsflags=nodocs install $buildDeps && \
	rm -rf /var/lib/apt/lists/* && \
	\
	wget -O redis.tar.gz "$REDIS_DOWNLOAD_URL" && \
	echo "$REDIS_DOWNLOAD_SHA *redis.tar.gz" | sha1sum -c - && \
	mkdir -p /usr/src/redis && \
	tar -xzf redis.tar.gz -C /usr/src/redis --strip-components=1 && \
	rm redis.tar.gz && \
	\
	make -C /usr/src/redis -j "$(nproc)" && \
	make -C /usr/src/redis install && \
	cp /usr/src/redis/src/redis-trib.rb /usr/local/bin && \
	rm -rf /usr/src/redis && \
	gem install redis && \
	yum clean all
	

COPY . /redis
COPY entrypoint.sh /usr/local/bin

RUN chmod 755 -R /redis && \
	chmod 755 /usr/local/bin/entrypoint.sh && \
	chown -R redis:redis /redis

VOLUME /redis
WORKDIR /redis

USER redis

CMD ["/bin/bash", "entrypoint.sh"]
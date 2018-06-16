#!/bin/bash

function launchcluster() {
  redis_conf=/redis/conf/redis.conf
  if [[ -n ${redis_ports} ]]; then
    ports=${redis_ports//,/ }
    for port in $ports
    do
      confPath=/redis/conf/redis${port}.conf
      cp ${redis_conf} ${confPath}
      sed -i "s/%port%/${port}/g" ${confPath}
      redis-server ${confPath}
    done
  else
    sed -i "s/%port%/6379/g" ${redis_conf}
    redis-server ${redis_conf}
  fi
  /bin/bash
}

function launchsentinel() {
  if [[ -n ${masters} ]]; then
    master=echo ${masters}|awk -F ',' '{print $1}'
    ip=echo master|awk -F ':' '{print $1}'
    port=echo master|awk -F ':' '{print $2}'
  else
    ip=127.0.0.1
    port=6379
  fi

  while true; do
    redis-cli -h ${ip} -p ${port} INFO
    if [[ "$?" == "0" ]]; then
      break
    fi
    echo "Connecting to master failed.  Waiting..."
    sleep 10
  done

  sentinel_conf=/redis/conf/sentinel.conf
  sed -i "s/%master-ip%/${ip}/g" ${sentinel_conf}
  sed -i "s/%master-port%/${port}/g" ${sentinel_conf}

  redis-sentinel ${sentinel_conf}
  /bin/bash
}


if [[ "${CLUSTER}" == "true" ]]; then
  launchcluster
  exit 0
fi

if [[ "${SENTINEL}" == "true" ]]; then
  launchsentinel
  exit 0
fi
yum -y --setopt=tsflags=nodocs install wget

docker network create --subnet 10.10.10.0/24 redisnet
docker build -t redis-cluster .

docker run --net redisnet --ip 10.10.10.100 -idt --name redis_server01 -e CLUSTER=true -e redis_ports=6379,6380,6381 -p 7000-7002:6379-6381 redis-cluster
docker run --net redisnet --ip 10.10.10.101 -idt --name redis_server02 -e CLUSTER=true -e redis_ports=6379,6380,6381 -p 7003-7005:6379-6381 redis-cluster
docker run --net redisnet --ip 10.10.10.102 -idt --name redis_server03 -e CLUSTER=true -e redis_ports=6379,6380,6381 -p 7006-7008:6379-6381 redis-cluster
redis-trib.rb create --replicas 0 10.10.10.100:6379 10.10.10.101:6380 10.10.10.102:6381

masterId=$(redis-trib.rb check 10.10.10.100:6379|grep 'M:'|grep '10.10.10.100:6379'|awk '{print $2}')
redis-trib.rb add-node --slave --master-id $masterId 10.10.10.101:6379 10.10.10.100:6379 
redis-trib.rb add-node --slave --master-id $masterId 10.10.10.102:6379 10.10.10.100:6379

masterId=$(redis-trib.rb check 10.10.10.100:6379|grep 'M:'|grep '10.10.10.101:6380'|awk '{print $2}')
redis-trib.rb add-node --slave --master-id $masterId 10.10.10.100:6380 10.10.10.100:6379
redis-trib.rb add-node --slave --master-id $masterId 10.10.10.102:6380 10.10.10.100:6379

masterId=$(redis-trib.rb check 10.10.10.100:6379|grep 'M:'|grep '10.10.10.102:6381'|awk '{print $2}')
redis-trib.rb add-node --slave --master-id $masterId 10.10.10.100:6381 10.10.10.100:6379
redis-trib.rb add-node --slave --master-id $masterId 10.10.10.101:6381 10.10.10.100:6379
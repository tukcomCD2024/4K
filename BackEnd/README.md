## 도커파일 실행 하는법

```
docker build -t [username]/[image name]:tag
```

```
docker tag [username]/[image name]:tag [username]/[image name]:latest
```


Jenkins CI/CD 구축 참고

https://velog.io/@haeny01/AWS-Jenkins%EB%A5%BC-%ED%99%9C%EC%9A%A9%ED%95%9C-Docker-x-SpringBoot-CICD-%EA%B5%AC%EC%B6%95#3-publish-over-ssh-%ED%94%8C%EB%9F%AC%EA%B7%B8%EC%9D%B8-%EC%84%A4%EC%A0%95

2.Jenkins 설치 및 실행 확인 에러
https://rainbound.tistory.com/entry/Ubuntu-jenkins-%EC%84%A4%EC%B9%98

# Jenkins x Github 연동

1. SSH 키 생성

```
$ sudo mkdir /var/lib/jenkins/.ssh
$ sudo ssh-keygen -t rsa -f /var/lib/jenkins/.ssh/jtogkey
```

2. github에 Deploy key 등록하기

```
$ cat /var/lib/jenkins/.ssh/jtogkey.pub
```

3. Jenkins Credential 등록하기

```
$ sudo cat /var/lib/jenkins/.ssh/jtogkey
```
```
-----BEGIN OPENSSH PRIVATE KEY-----
...
... 이와 같은 형태의 key가 private key 입니다 ...
...
-----END OPENSSH PRIVATE KEY-----
```

# Jenkins x 배포서버 SSH연결

1. SSH 키 생성
```
$ sudo ssh-keygen -t rsa -C "jtoekey" -m PEM -P "" -f /var/lib/jenkins/.ssh/jtoekey
// "jtoekey" 여기는 주석 key의 명칭 
```

2. 배포서버 PublicKey 등록
```
// 구축 서버
$ sudo cat /var/lib/jenkins/.ssh/jtoekey.pub

// 배포 서버
$ vi .ssh/authorized_keys
```
위에서 생성했던 jtoekey를 배포서버인 인스턴스의 .ssh/authorized_keys 파일에 추가 

3. Publish Over SSH 플러그인 설정

- Jenkins 플러그인에서 publish over ssh 설치

- Jenkins 관리 > 시스템 관리 아래 Publish Over SSH 영역의 고급 버튼을 눌러 확장하여 설정

### 여기서 이슈 발생 
sheel에서 [ubuntu@ip-private ipv4 주소] 일 때, 

- Path to Key 는 위에서 생성해준 private key 경로를 넣어주세요. 

`
/var/lib/jenkins/.ssh/jtoekey
`
- Key 는 Private key 파일안의 내용을 복사 붙여넣기 해주세요.

`
  $ sudo cat /var/lib/jenkins/.ssh/jtoekey
`

- Name 는 접속할 ssh 서버의 이름을 지어주세요.
  
`
그냥 접속할 이름 아무거나 써도 됌
`
- Hostname 는 접속할 인스턴스의 주소를 넣어주세요.

`
ec2 인스턴스 private Ipv4 주소 
`
- Username 는 접속할 유저명을 넣어주세요.

`
ubuntu
`


# Jenkins x Docker 설정

https://velog.io/@haeny01/AWS-EC2-X-Docker

apt 업데이트

```
$ sudo apt update
```

apt HTTPS 설정

```
$ sudo apt install apt-transport-https ca-certificates curl software-properties-common
```

도커 GPG키 등록

```
$ curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo apt-key add -
```

### apt에 도커 Repository 설정

1. 도커 Repository 추가

```
$ sudo add-apt-repository "deb [arch=amd64] https://download.docker.com/linux/ubuntu focal stable"
```

2. apt 업데이트

```
$ sudo apt update
```

3. 우분투 Repo 대신 도커 Repo로 설치하는지 확인

```
$ apt-cache policy docker-ce
```

# 도커 설치

- 도커 설치

```
$ sudo apt install docker-ce
```

- 도커 확인

```
$ sudo systemctl status docker
```
Active상태가 running 이면 실행중 
```
ubuntu@ip-172-31-17-241:~$ sudo systemctl status docker
● docker.service - Docker Application Container Engine
Loaded: loaded (/lib/systemd/system/docker.service; enabled; vendor preset: enabled)
Active: active (running) since Sun 2024-01-21 12:04:08 UTC; 46s ago
TriggeredBy: ● docker.socket
Docs: https://docs.docker.com
Main PID: 11459 (dockerd)
Tasks: 9
Memory: 35.2M
CPU: 412ms
CGroup: /system.slice/docker.service
└─11459 /usr/bin/dockerd -H fd:// --containerd=/run/containerd/containerd.sock
```

1. Docker 그룹에 젠킨스 추가

```
// 추가
$ sudo usermod -aG docker jenkins
$ su - jenkins

// 확인
$ id -nG
jenkins docker
```

```
su - jenkins
Password: 
su: Authentication failure
```

## 우분투 22버전부터 패스워드 생성 하고 입장해야함. 

인증이 안되면 
```
$  sudo passwd root
> New password:
> Retype new password:
> passwd: password updated successfully

$ sudo passwd jenkins
New password: 
Retype new password: 
passwd: password updated successfully
```


2. docker.sock 권한 변경

```
$ sudo chmod 666 /var/run/docker.sock
```

3. Jenkins에서 Docker login

```
$ sudo su - jenkins
$ docker login
```
도커허브의 아이디와 비밀번호 입력

# Jenkins Item
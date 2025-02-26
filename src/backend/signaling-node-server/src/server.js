import express from "express";
// import WebSocket from "ws";
import SocketIO from "socket.io";
import http from "http";

const PORT = process.env.PORT || 9090;

const server = express();

const httpServer = http.createServer(server);
const wsServer = SocketIO(httpServer);

let roomObjArr = [
  // {
  //   roomName,
  //   currentNum,
  //   users: [
  //     {
  //       socketId,
  //       nickname,
  //     },
  //   ],
  // },
];
const MAXIMUM = 5;

wsServer.on("connection", (socket) => {
  console.log("[NODE-SIGNAL] 연결 완료");

  let myRoomName = null;
  let myNickname = null;

    socket.on("join_room", ({roomName, nickname}) => {
        myRoomName = roomName;
        myNickname = nickname;

        let isRoomExist = false;
        let targetRoomObj = null;

        for (let i = 0; i < roomObjArr.length; ++i) {
            if (roomObjArr[i].roomName === roomName) {
                if (roomObjArr[i].currentNum >= MAXIMUM) {
                    socket.emit("channel_full"); // 방이 가득 찬 경우 클라이언트에게 알림
                    return;
                }
                isRoomExist = true;
                targetRoomObj = roomObjArr[i];
                break;
            }
        }

        if (!isRoomExist) {
            targetRoomObj = {
                roomName,
                currentNum: 0,
                users: [],
            };
            roomObjArr.push(targetRoomObj);
        }

        targetRoomObj.users.push({
            socketId: socket.id,
            nickname,
        });
        ++targetRoomObj.currentNum;

        socket.join(roomName);

        // 본인에게 기존 멤버 정보 전달 (EXIST_USERS)
        const participants = targetRoomObj.users.map(user => user.nickname);
        socket.emit("exist_users", {
            channelId: roomName,
            participants
        });

        // 전체 채널에 새로운 멤버 입장 알림 (USER_JOINED)
        socket.to(roomName).emit("user_joined", {
            channelId: roomName,
            senderId: nickname
        });

        console.log(`[NODE-SIGNAL] ${nickname} joined channel: ${roomName}`);
    });

  socket.on("offer", ({offer, remoteSocketId, localNickname}) => {
    socket.to(remoteSocketId).emit("offer", offer, socket.id, localNickname);

    console.log("[NODE-SIGNAL] Offer");
  });

  socket.on("answer", ({answer, remoteSocketId}) => {
    socket.to(remoteSocketId).emit("answer", answer, socket.id);

    console.log("[NODE-SIGNAL] Answer");
  });

  socket.on("ice", ({ice, remoteSocketId}) => {
    socket.to(remoteSocketId).emit("ice", ice, socket.id);

    console.log("[NODE-SIGNAL] Ice");
  });

  socket.on("chat", ({message, roomName}) => {
    socket.to(roomName).emit("chat", message);

    console.log("[NODE-SIGNAL] Chat");
  });

  socket.on("disconnecting", () => {
      if (!myRoomName) return;

      // 퇴장 이벤트 전송
      socket.to(myRoomName).emit("user_left", {
          channelId: myRoomName,
          senderId: myNickname
      });

      // 현재 방에서 유저 제거
      for (let i = 0; i < roomObjArr.length; ++i) {
          if (roomObjArr[i].roomName === myRoomName) {
              roomObjArr[i].users = roomObjArr[i].users.filter(user => user.socketId !== socket.id);
              --roomObjArr[i].currentNum;

              if (roomObjArr[i].currentNum === 0) {
                  roomObjArr.splice(i, 1); // 방 삭제
              }
              break;
          }
      }

      console.log(`[NODE-SIGNAL] ${myNickname} left channel: ${myRoomName}`);
    });
});

const handleListen = () =>
    console.log(`✅ Listening on http://localhost:${PORT}`);
httpServer.listen(PORT, handleListen);

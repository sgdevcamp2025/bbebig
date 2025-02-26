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
  console.log("[Node-Signal] 연결 완료");

  let myRoomName = null;
  let myNickname = null;

  socket.on("join_room", (roomName, nickname) => {
    myRoomName = roomName;
    myNickname = nickname;

    let isRoomExist = false;
    let targetRoomObj = null;

    console.log("[Node-Signal] 방 참여");

    // forEach를 사용하지 않는 이유: callback함수를 사용하기 때문에 return이 효용없음.
    for (let i = 0; i < roomObjArr.length; ++i) {
      if (roomObjArr[i].roomName === roomName) {
        // Reject join the room
        if (roomObjArr[i].currentNum >= MAXIMUM) {
          socket.emit("reject_join");

          console.log("[Node-Signal] 방 참여자가 최대, 참여 거부");
          return;
        }

        isRoomExist = true;
        targetRoomObj = roomObjArr[i];
        break;
      }
    }

    // Create room
    if (!isRoomExist) {
      targetRoomObj = {
        roomName,
        currentNum: 0,
        users: [],
      };
      roomObjArr.push(targetRoomObj);
    }

    //Join the room
    targetRoomObj.users.push({
      socketId: socket.id,
      nickname,
    });
    ++targetRoomObj.currentNum;

    socket.join(roomName);
    socket.emit("accept_join", targetRoomObj.users);

    console.log("[Node-Signal] 방 참여 승인");
  });

  socket.on("offer", (offer, remoteSocketId, localNickname) => {
    socket.to(remoteSocketId).emit("offer", offer, socket.id, localNickname);

    console.log("[Node-Signal] Offer");
  });

  socket.on("answer", (answer, remoteSocketId) => {
    socket.to(remoteSocketId).emit("answer", answer, socket.id);

    console.log("[Node-Signal] Answer");
  });

  socket.on("ice", (ice, remoteSocketId) => {
    socket.to(remoteSocketId).emit("ice", ice, socket.id);

    console.log("[Node-Signal] Ice");
  });

  socket.on("chat", (message, roomName) => {
    socket.to(roomName).emit("chat", message);

    console.log("[Node-Signal] Chat");
  });

  socket.on("disconnecting", () => {
    socket.to(myRoomName).emit("leave_room", socket.id, myNickname);

    let isRoomEmpty = false;
    for (let i = 0; i < roomObjArr.length; ++i) {
      if (roomObjArr[i].roomName === myRoomName) {
        const newUsers = roomObjArr[i].users.filter(
            (user) => user.socketId != socket.id
        );
        roomObjArr[i].users = newUsers;
        --roomObjArr[i].currentNum;

        if (roomObjArr[i].currentNum == 0) {
          isRoomEmpty = true;
        }
      }
    }

    // Delete room
    if (isRoomEmpty) {
      const newRoomObjArr = roomObjArr.filter(
          (roomObj) => roomObj.currentNum != 0
      );
      roomObjArr = newRoomObjArr;
    }

    console.log("[Node-Signal] 방 떠나기");
  });
});

const handleListen = () =>
    console.log(`✅ Listening on http://localhost:${PORT}`);
httpServer.listen(PORT, handleListen);

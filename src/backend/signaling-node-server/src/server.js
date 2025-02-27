import http from "http";
import express from "express";
import cors from "cors";
import { Server } from "socket.io";
import { EurekaClient } from "./libs/eureka";

const app = express();
const PORT = 9090;

// CORS 설정
app.use(
  cors({
    origin: "http://localhost:5173",
    methods: ["GET", "POST"],
    credentials: true,
  })
);

app.get("/", (req, res) => {
  res.send("Hello from Express server with CORS enabled!");
});

const httpServer = http.createServer(app);
const wsServer = new Server(httpServer, {
  cors: {
    origin: ["http://localhost:5173", "https://bbebig.netlify.app/"],
    methods: ["GET", "POST"],
    credentials: true,
  },
});

const eurekaConfig = {
  instance: {
    app: "SIGNALING-NODE-SERVER",
    hostName: "signaling—node-server",
    ipAddr: "signaling-node-server",
    status: "UP",
    port: {
      $: 9090,
      "@enabled": "true",
    },
    vipAddress: "signaling-node-server",
    statusPageUrl: "http://signaling-node-server:9090/",
    dataCenterInfo: {
      "@class": "com.netflix.appinfo.InstanceInfo$DefaultDataCenterInfo",
      name: "MyOwn",
    },
  },
  eureka: {
    host: "discovery-server",
    port: 8761,
    servicePath: "/eureka/apps",
  },
};

const eurekaClient = new EurekaClient(eurekaConfig);

wsServer.on("connection", (socket) => {
  console.log("✅ A client connected:", socket.id);

  // 방에 입장
  socket.on("join_room", ({ roomName, userId }) => {
    socket.join(roomName);
    socket.roomName = roomName;
    socket.userId = userId;
    // 이미 방에 있는 사람들에게 "새로운 유저" 알림
    socket.to(roomName).emit("welcome", socket.id, userId);
  });

  // WebRTC offer
  socket.on("offer", (offer, remoteId) => {
    // remoteId에게 offer와 sender의 userId 전달
    wsServer.to(remoteId).emit("offer", offer, socket.id, socket.userId);
  });

  // WebRTC answer
  socket.on("answer", (answer, remoteId) => {
    wsServer.to(remoteId).emit("answer", answer, socket.id, socket.userId);
  });

  // ICE candidate
  socket.on("ice", (ice, remoteId) => {
    wsServer.to(remoteId).emit("ice", ice, socket.id, socket.userId);
  });

  // 연결 해제
  socket.on("disconnect", () => {
    console.log("❌ Client disconnected:", socket.id);
    if (socket.roomName) {
      socket.to(socket.roomName).emit("user_left", socket.id);
    }
  });
});

const start = async () => {
  try {
    await eurekaClient.register();
    httpServer.listen(PORT, () => {
      console.log(`✅ Server is running on http://localhost:${PORT}`);
    });
  } catch (error) {
    eurekaClient.deregister();
    process.exit(1);
  }
};

start();

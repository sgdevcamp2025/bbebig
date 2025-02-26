import http from "http";
import express from "express";
import { Server } from "socket.io";

const PORT = 9090;

const app = express();
const httpServer = http.createServer(app);
const wsServer = new Server(httpServer, {
    cors: {
        origin: ["http://localhost:5173", "https://bbebig.netlify.app"], // 클라이언트 주소
        methods: ["GET", "POST"],
        credentials: true,
    },
});

// 메모리 내에서 방 별 유저 관리
const roomUsers = {};

// 기본 API 라우트
app.get("/", (req, res) => {
    res.send("Hello from Express server with CORS enabled!");
});

// 특정 방의 유저 목록 조회 API
app.get("/api/signal/participants/:roomId", (req, res) => {
    const { roomId } = req.params;
    console.log("API CALL");
    const users = roomUsers[roomId] || [];
    res.json({ roomId, users });
});

wsServer.on("connection", (socket) => {
    console.log("✅ A client connected:", socket.id);

    socket.on("join_room", ({ roomId, userId }) => {
        socket.join(roomId);
        socket.roomId = roomId;
        socket.userId = userId;

        // 메모리에서 방에 속한 유저 목록 관리
        if (!roomUsers[roomId]) {
            roomUsers[roomId] = [];
        }
        roomUsers[roomId].push(userId);

        // 해당 방의 모든 사용자 목록을 새로고침하도록 emit
        wsServer.to(roomId).emit("update_user_list", roomUsers[roomId]);

        socket.to(roomId).emit("welcome", socket.id, userId);
    });

    // WebRTC offer
    socket.on("offer", (offer, remoteId) => {
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

    socket.on("disconnect", () => {
        console.log("❌ Client disconnected:", socket.id);

        if (socket.roomId && socket.userId) {
            // 해당 방에서 유저 제거
            if (roomUsers[socket.roomId]) {
                roomUsers[socket.roomId] = roomUsers[socket.roomId].filter(user => user !== socket.userId);
                if (roomUsers[socket.roomId].length === 0) {
                    delete roomUsers[socket.roomId]; // 방이 비면 삭제
                }
            }

            // 방의 새로운 사용자 목록을 클라이언트에 업데이트
            wsServer.to(socket.roomId).emit("update_user_list", roomUsers[socket.roomId] || []);
            socket.to(socket.roomId).emit("user_left", socket.id);
        }
    });
});

httpServer.listen(PORT, () => {
    console.log(`✅ Server is running on http://localhost:${PORT}`);
});

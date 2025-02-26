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
    console.log("[NODE-SIGNAL] GET 요청")
    res.send("Hello from Express server with CORS enabled!");
});

// 특정 방의 유저 목록 조회 API
app.get("/api/signal/participants/:roomId", (req, res) => {
    const { roomId } = req.params;
    console.log("[NODE-SIGNAL] API CALL - /api/signal/participants/${roomId}");
    const users = roomUsers[roomId] || [];
    res.json({ roomId, users });
});

wsServer.on("connection", (socket) => {
    console.log(`[NODE-SIGNAL] A client connected: ${socket.id}`);

    socket.on("join_room", ({ roomId, userId }) => {
        console.log(`[NODE-SIGNAL] User joined room: roomId=${roomId}, userId=${userId}, socketId=${socket.id}`);

        socket.join(roomId);
        socket.roomId = roomId;
        socket.userId = userId;

        // 메모리에서 방에 속한 유저 목록 관리
        if (!roomUsers[roomId]) {
            roomUsers[roomId] = [];
        }
        roomUsers[roomId].push(userId);

        console.log(`[NODE-SIGNAL] Current users in room ${roomId}:`, roomUsers[roomId]);

        // 해당 방의 모든 사용자 목록을 새로고침하도록 emit
        wsServer.to(roomId).emit("update_user_list", roomUsers[roomId]);
        socket.to(roomId).emit("welcome", socket.id, userId);
    });

    // WebRTC offer
    socket.on("offer", (offer, remoteId) => {
        console.log(`[NODE-SIGNAL] offer sent: from ${socket.id} (userId=${socket.userId}) to ${remoteId}`);
        wsServer.to(remoteId).emit("offer", offer, socket.id, socket.userId);
    });

    // WebRTC answer
    socket.on("answer", (answer, remoteId) => {
        console.log(`[NODE-SIGNAL] sent: from ${socket.id} (userId=${socket.userId}) to ${remoteId}`);
        wsServer.to(remoteId).emit("answer", answer, socket.id, socket.userId);
    });

    // ICE candidate
    socket.on("ice", (ice, remoteId) => {
        console.log(`[NODE-SIGNAL] ICE candidate sent: from ${socket.id} (userId=${socket.userId}) to ${remoteId}`);
        wsServer.to(remoteId).emit("ice", ice, socket.id, socket.userId);
    });

    socket.on("disconnect", () => {
        console.log(`[NODE-SIGNAL] Client disconnected: ${socket.id}, roomId=${socket.roomId}, userId=${socket.userId}`);

        if (socket.roomId && socket.userId) {
            // 해당 방에서 유저 제거
            if (roomUsers[socket.roomId]) {
                roomUsers[socket.roomId] = roomUsers[socket.roomId].filter(user => user !== socket.userId);
                if (roomUsers[socket.roomId].length === 0) {
                    delete roomUsers[socket.roomId]; // 방이 비면 삭제
                }
            }

            console.log(`[NODE-SIGNAL] Updated users in room ${socket.roomId}:`, roomUsers[socket.roomId] || []);

            // 방의 새로운 사용자 목록을 클라이언트에 업데이트
            wsServer.to(socket.roomId).emit("update_user_list", roomUsers[socket.roomId] || []);
            socket.to(socket.roomId).emit("user_left", socket.id);
        }
    });
});

httpServer.listen(PORT, () => {
    console.log(`[NODE-SIGNAL] 서버가 :${PORT}에서 실행 중입니다.`);
});

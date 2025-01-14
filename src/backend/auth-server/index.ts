import fastify from 'fastify';

const app = fastify({
  logger: true,
});

app.get('/', (req, res) => {
  res.send('Hello World');
});

app.listen({ port: 3000 }, (err, address) => {
  console.log('Server is running on port 3000');
});


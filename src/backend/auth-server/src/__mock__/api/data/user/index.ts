type User = {
  id: string;
  email: string;
  password: string;
};

const userList: Map<string, User> = new Map();

userList.set('test@test.com', { id: '1', email: 'test@test.com', password: '1234' });
userList.set('test2@test.com', { id: '2', email: 'test2@test.com', password: '1234' });
userList.set('test3@test.com', { id: '3', email: 'test3@test.com', password: '1234' });

export default userList;

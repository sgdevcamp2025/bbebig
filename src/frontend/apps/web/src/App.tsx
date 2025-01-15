import './globals.css';
import '@repo/ui/styles.css';
import { Counter, Header } from '@repo/ui';

function App() {
  return (
    <div className='text-red-800'>
      <Counter />
      <Header title={'안냐세요'} />
    </div>
  );
}

export default App;

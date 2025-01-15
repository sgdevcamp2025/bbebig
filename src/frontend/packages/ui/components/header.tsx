export function Header({ title }: { title: string }) {
  return (
    <header
      id='header'
      className='ui-bg-red-300'>
      <h1>${title}</h1>
    </header>
  );
}

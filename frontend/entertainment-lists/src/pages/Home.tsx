import { Link } from "react-router-dom";

function Home() {
  return (
    <main className="flex-1 flex items-center justify-center">
      <ul className="flex flex-col sm:flex-row sm:space-x-4 space-y-2 sm:space-y-0">
        <li>
          <Link
            to="/anime"
            className="w-32 bg-blue-600 rounded px-4 py-2 text-xl text-white hover:bg-blue-800 transition-colors duration-400 hover:cursor-pointer"
          >
            Anime
          </Link>
        </li>
        <li>
          <Link
            to="/manga"
            className="w-32 bg-blue-600 rounded px-4 py-2 text-xl text-white hover:bg-blue-800 transition-colors duration-400 hover:cursor-pointer"
          >
            Manga
          </Link>
        </li>
      </ul>
    </main>
  );
}

export default Home;

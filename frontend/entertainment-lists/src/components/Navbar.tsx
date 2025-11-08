import { Link } from "react-router-dom";

function Navbar() {
  return (
    <nav className="bg-gray-600 flex justify-between items-center p-4">
      <h1 className="px-4 py-2 text-2xl text-white">
        Entertainment Lists - Kitsu Implementation
      </h1>
      <ul className="hidden md:flex space-x-4">
        <li>
          <Link
            to="/"
            className="bg-blue-600 rounded px-4 py-2 text-xl text-white hover:bg-blue-800 transition-colors duration-400 hover:cursor-pointer"
          >
            Home
          </Link>
        </li>
      </ul>
    </nav>
  );
}

export default Navbar;

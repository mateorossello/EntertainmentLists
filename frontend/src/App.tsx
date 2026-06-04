import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import Navbar from "./components/Navbar";
import Home from "./pages/Home";
import Entities from "./pages/Entities";

const queryClient = new QueryClient();

function App() {
  return (
    <QueryClientProvider client={queryClient}>
      <Router>
        <div className="min-h-screen flex flex-col">
          <Navbar />

          <Routes>
            <Route path="/" element={<Home />} />

            <Route path="/anime" element={<Entities type="anime" />} />

            <Route path="/manga" element={<Entities type="manga" />} />
          </Routes>
        </div>
      </Router>
    </QueryClientProvider>
  );
}

export default App;

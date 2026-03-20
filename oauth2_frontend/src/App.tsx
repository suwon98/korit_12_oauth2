import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom"
import { QueryClient, QueryClientProvider } from "@tanstack/react-query"
import HomePage from "./pages/HomePage"
import LoginPage from "./pages/LoginPage"
import SignupPage from "./pages/SignupPage"
import { AuthProvider } from "./store/authStore"
import OAuth2CallbackPage from "./pages/OAuth2CallbackPage"

const queryClient = new QueryClient();

function App() {
  return (
    <QueryClientProvider client={queryClient}>
        <BrowserRouter>
          <AuthProvider>
            <Routes>
              <Route path='/login' element={<LoginPage />} />
              <Route path='/signup' element={<SignupPage />} />
              <Route path='/oauth2/callback' element={<OAuth2CallbackPage />} />
              <Route path='/' element={<HomePage />} />
              <Route path='*' element={<Navigate to='/login' />} />
            </Routes>
          </AuthProvider>
        </BrowserRouter>
    </QueryClientProvider>
  )
}


export default App
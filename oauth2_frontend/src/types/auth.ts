export interface SignupRequest {
  email: string;
  password: string;
  name: string;
}

export interface LoginRequest {
  email: string;
  password: string;
}

export interface AuthResponse {
  token: string;
  email: string;
  name: string;
  role: string;
}

// localStorage와 Context에 저장하는 사용자 정보
// token은 별개로 저장할겁니다.
export interface UserInfo {
  email: string;
  name: string;
  role: string;
}

// Partial<T> : T의 모든 field를 선택적(Optional)으로 만들어줍니다.

export type SignupFormErrors = Partial<SignupRequest>;
export type LoginFormErrors = Partial<LoginRequest>;

// Context Type
export interface AuthContextType {
  user: UserInfo | null;
  login: (token : string, userInfo : UserInfo) => void;
  logout: () => void;
  getToken: () => string | null;
  isLoggedIn: boolean;
}
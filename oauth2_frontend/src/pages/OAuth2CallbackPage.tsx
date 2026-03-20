import { useEffect, useState } from "react"
import { useNavigate, useSearchParams } from "react-router-dom"
import { Box, CircularProgress, Typography, Alert } from "@mui/material"
import { useAuth } from "../store/authStore";

export default function OAuth2CallbackPage () {
  const navigate = useNavigate();
  const [searchParams] = useSearchParams();
  const {login} = useAuth();
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const token = searchParams.get('token');
    const email = searchParams.get('email');
    const name = searchParams.get('name');
    const role = searchParams.get('role') ?? 'ROLE_USER';

    if(!token) {
      setError('로그인 중 오류가 발생했습니다. 다시 시도해주세요.')
      return;
    }
    // 여기까지 실행되면 token이 null이 아니므로 추가 단계가 가능하긴 합니다. 그런데 email / name이 null값일 수도 있겠네요
    login(token, {
      email: email ?? '',
      name: name ?? '',
      role,
    });
    navigate('/', {replace: true});
  }, [searchParams]);

  if(error) {
    return(
    <>
      <Box sx={{mt: 8, textAlign: 'center'}}>
        <Alert severity= 'error' sx={{maxWidth: 400, mx: 'auto'}}>{error}</Alert>
      </Box>
    </>
    );
  }
  return (
    <Box sx={{mt: 8, display: 'flex', flexDirection: 'column', alignItems: 'center', gap: 2}}>
      <CircularProgress size={48}/>
      <Typography color='text.secondary'>구글 로그인 처리중 ...</Typography>
    </Box>
  )
}
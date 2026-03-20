import { Container, Box, Typography, Button, Paper, Avatar } from "@mui/material"
import LogoutIcon from '@mui/icons-material/Logout';
import PersonIcon from '@mui/icons-material/Person';
import { useAuth } from "../store/authStore";
import { useNavigate } from "react-router-dom";

export default function HomePage () {
  // 정의한 useAuth()를 사용해볼겁니다. 이하 const {user, logout}는 custom hook 만든거
  const {user, logout} = useAuth(); // 전역 관리를 ContextAPI를 통해서 구현했다는 걸 알 수 있습니다.
  const navigate = useNavigate();

  const handleLogout = () : void => {
    logout();
    navigate('/login');
  }

  return (
    <Container maxWidth='sm'>
      <Box sx={{mt: 8}}>
        <Paper elevation={3} sx={{p: 4, textAlign: 'center'}}>
          <Avatar sx={{width: 80, height: 80, mx: 'auto', mb: 2, bgcolor: 'primary.main'}}>
            <PersonIcon fontSize="large" />
          </Avatar>
          {/* user?.name: user가 UserInfo 자료형을 따르는 객체거나 null이라는 뜻인데, null이면 name이 없습니다. 그 때 오류 발생 안하고 undefined를 리턴해주게됩니다. */}
          <Typography variant="h5" fontWeight='bold' gutterBottom>환영합니다, {user?.name} 님 !</Typography>
          <Box sx={{textAlign: 'left', bgcolor: 'grey.50', p: 2, borderRadius: 1, mb: 3}}>
            <Typography variant="body2" color='text.secondary'>이메일</Typography>
            <Typography variant="body1" fontWeight='medium'>{user?.email}</Typography>
            <Typography variant="body2" color='text.secondary'>권한</Typography>
            <Typography variant="body1" fontWeight='medium'>{user?.role}</Typography>
          </Box>
          <Button
            variant="outlined"
            color="error"
            startIcon={<LogoutIcon />}
            onClick={handleLogout}
            fullWidth
          >
            Logout
          </Button>
        </Paper>
      </Box>
    </Container>
  )
}
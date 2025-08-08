import { useState } from 'react';
import { ThemeProvider, createTheme } from '@mui/material/styles';
import { CssBaseline, Container, Box } from '@mui/material';
import LoginForm from './components/LoginForm';
import RegistrationForm from './components/RegistrationForm';
import Dashboard from './components/Dashboard';

const theme = createTheme({
  palette: {
    primary: {
      main: '#2e7d32',
    },
    secondary: {
      main: '#f50057',
    },
    background: {
      default: '#f5f5f5',
      paper: '#ffffff',
    },
  },
  typography: {
    fontFamily: '"Roboto", "Helvetica", "Arial", sans-serif',
    h4: {
      fontWeight: 600,
    },
  },
  components: {
    MuiTextField: {
      styleOverrides: {
        root: {
          '& .MuiOutlinedInput-root': {
            '&:hover fieldset': {
              borderColor: '#2e7d32',
            },
            '&.Mui-focused fieldset': {
              borderColor: '#2e7d32',
            },
          },
        },
      },
    },
    MuiButton: {
      styleOverrides: {
        root: {
          textTransform: 'none',
          borderRadius: 8,
          padding: '12px 24px',
          fontSize: '1rem',
          fontWeight: 500,
        },
      },
    },
  },
});

function App() {
  const [currentView, setCurrentView] = useState('login'); // 'login', 'registration', 'dashboard'
  const [isGuestMode, setIsGuestMode] = useState(false);

  const handleLoginSuccess = () => {
    setCurrentView('dashboard');
    setIsGuestMode(false);
  };

  const handleGuestLogin = () => {
    setCurrentView('dashboard');
    setIsGuestMode(true);
  };

  const handleRegistrationSuccess = () => {
    setCurrentView('login');
  };

  const handleBackToLogin = () => {
    setCurrentView('login');
  };

  const handleShowRegistration = () => {
    setCurrentView('registration');
  };

  const handleLogout = () => {
    setCurrentView('login');
    setIsGuestMode(false);
  };

  const renderCurrentView = () => {
    switch (currentView) {
      case 'login':
        return (
          <LoginForm 
            onLoginSuccess={handleLoginSuccess}
            onBackToLogin={handleShowRegistration}
            onGuestLogin={handleGuestLogin}
          />
        );
      case 'registration':
        return (
          <RegistrationForm 
            onRegistrationSuccess={handleRegistrationSuccess}
            onBackToLogin={handleBackToLogin}
          />
        );
      case 'dashboard':
        return <Dashboard onLogout={handleLogout} isGuestMode={isGuestMode} />;
      default:
        return (
          <LoginForm 
            onLoginSuccess={handleLoginSuccess}
            onBackToLogin={handleShowRegistration}
            onGuestLogin={handleGuestLogin}
          />
        );
    }
  };

  return (
    <ThemeProvider theme={theme}>
      <CssBaseline />
      <Box sx={{ 
        minHeight: '100vh', 
        background: currentView === 'dashboard' 
          ? '#f5f5f5' 
          : 'linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%)',
        display: 'flex',
        alignItems: currentView === 'dashboard' ? 'flex-start' : 'center',
        justifyContent: 'center',
        padding: currentView === 'dashboard' ? 0 : 2
      }}>
        <Container maxWidth={currentView === 'dashboard' ? false : 'sm'}>
          {renderCurrentView()}
        </Container>
      </Box>
    </ThemeProvider>
  );
}

export default App

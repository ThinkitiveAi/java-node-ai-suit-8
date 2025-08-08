import { useState } from 'react';
import { ThemeProvider, createTheme } from '@mui/material/styles';
import { CssBaseline, Container, Box } from '@mui/material';
import LoginForm from './components/LoginForm';
import RegistrationForm from './components/RegistrationForm';
import Dashboard from './components/Dashboard';

const theme = createTheme({
  palette: {
    primary: {
      main: '#1976d2',
      light: '#42a5f5',
      dark: '#1565c0',
    },
    secondary: {
      main: '#dc004e',
    },
    background: {
      default: '#f8fafc',
      paper: '#ffffff',
    },
    text: {
      primary: '#1e293b',
      secondary: '#64748b',
    },
  },
  typography: {
    fontFamily: '"Inter", "Roboto", "Helvetica", "Arial", sans-serif',
    h1: {
      fontWeight: 700,
      fontSize: '2.5rem',
    },
    h2: {
      fontWeight: 600,
      fontSize: '2rem',
    },
    h3: {
      fontWeight: 600,
      fontSize: '1.75rem',
    },
    h4: {
      fontWeight: 600,
      fontSize: '1.5rem',
    },
    h5: {
      fontWeight: 600,
      fontSize: '1.25rem',
    },
    h6: {
      fontWeight: 600,
      fontSize: '1.125rem',
    },
  },
  shape: {
    borderRadius: 12,
  },
  components: {
    MuiButton: {
      styleOverrides: {
        root: {
          textTransform: 'none',
          borderRadius: 8,
          padding: '12px 24px',
          fontSize: '0.875rem',
          fontWeight: 600,
          boxShadow: 'none',
          '&:hover': {
            boxShadow: '0 4px 12px rgba(25, 118, 210, 0.3)',
          },
        },
        contained: {
          background: 'linear-gradient(135deg, #1976d2 0%, #42a5f5 100%)',
          '&:hover': {
            background: 'linear-gradient(135deg, #1565c0 0%, #1976d2 100%)',
          },
        },
      },
    },
    MuiCard: {
      styleOverrides: {
        root: {
          borderRadius: 16,
          boxShadow: '0 4px 20px rgba(0, 0, 0, 0.08)',
          border: '1px solid rgba(0, 0, 0, 0.04)',
        },
      },
    },
    MuiPaper: {
      styleOverrides: {
        root: {
          borderRadius: 16,
        },
      },
    },
    MuiTextField: {
      styleOverrides: {
        root: {
          '& .MuiOutlinedInput-root': {
            borderRadius: 8,
            '&:hover fieldset': {
              borderColor: '#1976d2',
            },
            '&.Mui-focused fieldset': {
              borderColor: '#1976d2',
            },
          },
        },
      },
    },
    MuiAppBar: {
      styleOverrides: {
        root: {
          background: 'linear-gradient(135deg, #1976d2 0%, #42a5f5 100%)',
          boxShadow: '0 4px 20px rgba(25, 118, 210, 0.3)',
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
          ? '#f8fafc' 
          : 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
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

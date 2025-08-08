import { useState } from 'react';
import {
  Paper,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Typography,
  Box,
  Chip,
  Fade,
  Slide,
  IconButton,
  Tooltip,
  Avatar
} from '@mui/material';
import {
  Email,
  Phone,
  LocationOn,
  Person,
  LocalHospital
} from '@mui/icons-material';

const ProvidersList = ({ providers }) => {
  const [hoveredRow, setHoveredRow] = useState(null);

  const getInitials = (firstName, lastName) => {
    return `${firstName.charAt(0)}${lastName.charAt(0)}`.toUpperCase();
  };

  const getSpecializationColor = (specialization) => {
    const colors = {
      'Cardiology': '#f44336',
      'Neurology': '#2196f3',
      'Pediatrics': '#4caf50',
      'Orthopedics': '#ff9800',
      'Dermatology': '#9c27b0',
      'General Practice': '#607d8b',
      'Gynecology': '#e91e63',
      'Oncology': '#795548',
      'Ophthalmology': '#00bcd4',
      'Psychiatry': '#8bc34a',
      'Radiology': '#ff5722',
      'Surgery': '#3f51b5',
      'Urology': '#009688',
      'Endocrinology': '#ffc107',
      'Gastroenterology': '#673ab7'
    };
    return colors[specialization] || '#607d8b';
  };

  return (
    <Fade in={true} timeout={800}>
      <Box>
        <Slide direction="up" in={true} timeout={600}>
          <Box sx={{ mb: 3 }}>
            <Typography variant="h4" component="h2" gutterBottom sx={{ 
              fontWeight: 600,
              color: 'primary.main',
              display: 'flex',
              alignItems: 'center',
              gap: 1
            }}>
              <LocalHospital />
              Healthcare Providers
            </Typography>
            <Typography variant="body1" color="text.secondary">
              Manage and view all registered healthcare providers
            </Typography>
          </Box>
        </Slide>

        <Slide direction="up" in={true} timeout={800}>
          <Paper 
            elevation={2} 
            sx={{ 
              borderRadius: 3,
              overflow: 'hidden',
              boxShadow: '0 4px 20px rgba(0,0,0,0.1)',
              transition: 'all 0.3s ease',
              '&:hover': {
                boxShadow: '0 8px 30px rgba(0,0,0,0.15)',
              }
            }}
          >
            <TableContainer>
              <Table>
                <TableHead>
                  <TableRow sx={{ backgroundColor: 'primary.main' }}>
                    <TableCell sx={{ color: 'white', fontWeight: 600, fontSize: '1rem' }}>
                      Provider
                    </TableCell>
                    <TableCell sx={{ color: 'white', fontWeight: 600, fontSize: '1rem' }}>
                      Specialization
                    </TableCell>
                    <TableCell sx={{ color: 'white', fontWeight: 600, fontSize: '1rem' }}>
                      Contact
                    </TableCell>
                    <TableCell sx={{ color: 'white', fontWeight: 600, fontSize: '1rem' }}>
                      License
                    </TableCell>
                    <TableCell sx={{ color: 'white', fontWeight: 600, fontSize: '1rem' }}>
                      Location
                    </TableCell>
                  </TableRow>
                </TableHead>
                <TableBody>
                  {providers.map((provider, index) => (
                    <Fade in={true} timeout={1000 + index * 100} key={provider.id}>
                      <TableRow
                        sx={{
                          transition: 'all 0.3s ease',
                          cursor: 'pointer',
                          backgroundColor: hoveredRow === provider.id ? 'rgba(46, 125, 50, 0.04)' : 'transparent',
                          '&:hover': {
                            backgroundColor: 'rgba(46, 125, 50, 0.08)',
                            transform: 'translateY(-2px)',
                            boxShadow: '0 4px 12px rgba(46, 125, 50, 0.15)',
                          },
                        }}
                        onMouseEnter={() => setHoveredRow(provider.id)}
                        onMouseLeave={() => setHoveredRow(null)}
                      >
                        <TableCell>
                          <Box sx={{ display: 'flex', alignItems: 'center', gap: 2 }}>
                            <Avatar 
                              sx={{ 
                                bgcolor: getSpecializationColor(provider.specialization),
                                width: 48,
                                height: 48,
                                fontSize: '1.2rem',
                                fontWeight: 600
                              }}
                            >
                              {getInitials(provider.firstName, provider.lastName)}
                            </Avatar>
                            <Box>
                              <Typography variant="subtitle1" sx={{ fontWeight: 600 }}>
                                {provider.firstName} {provider.lastName}
                              </Typography>
                              <Typography variant="body2" color="text.secondary">
                                {provider.yearsExperience} years experience
                              </Typography>
                            </Box>
                          </Box>
                        </TableCell>
                        
                        <TableCell>
                          <Chip
                            label={provider.specialization}
                            sx={{
                              backgroundColor: getSpecializationColor(provider.specialization),
                              color: 'white',
                              fontWeight: 600,
                              fontSize: '0.875rem',
                              '&:hover': {
                                opacity: 0.9,
                              }
                            }}
                          />
                        </TableCell>
                        
                        <TableCell>
                          <Box sx={{ display: 'flex', flexDirection: 'column', gap: 1 }}>
                            <Box sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
                              <Email sx={{ fontSize: 16, color: 'action.active' }} />
                              <Typography variant="body2" sx={{ fontSize: '0.875rem' }}>
                                {provider.email}
                              </Typography>
                            </Box>
                            <Box sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
                              <Phone sx={{ fontSize: 16, color: 'action.active' }} />
                              <Typography variant="body2" sx={{ fontSize: '0.875rem' }}>
                                {provider.phone}
                              </Typography>
                            </Box>
                          </Box>
                        </TableCell>
                        
                        <TableCell>
                          <Typography variant="body2" sx={{ 
                            fontWeight: 600,
                            color: 'primary.main',
                            fontFamily: 'monospace',
                            fontSize: '0.875rem'
                          }}>
                            {provider.licenseNumber}
                          </Typography>
                        </TableCell>
                        
                        <TableCell>
                          <Box sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
                            <LocationOn sx={{ fontSize: 16, color: 'action.active' }} />
                            <Typography variant="body2" sx={{ fontSize: '0.875rem' }}>
                              {provider.city}, {provider.state}
                            </Typography>
                          </Box>
                        </TableCell>
                      </TableRow>
                    </Fade>
                  ))}
                </TableBody>
              </Table>
            </TableContainer>
          </Paper>
        </Slide>
      </Box>
    </Fade>
  );
};

export default ProvidersList; 
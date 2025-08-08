import { useState } from 'react';
import {
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  Button,
  TextField,
  Grid,
  FormControl,
  InputLabel,
  Select,
  MenuItem,
  Alert,
  CircularProgress,
  InputAdornment,
  Fade,
  Slide,
  Typography,
  Box,
  Stepper,
  Step,
  StepLabel,
  IconButton
} from '@mui/material';
import {
  Close,
  Person,
  Email,
  Phone,
  Business,
  LocationOn,
  School,
  Work,
  Home
} from '@mui/icons-material';
import { specializations } from '../utils/providersData';

const steps = ['Personal Information', 'Professional Details', 'Clinic Address'];

const AddProviderModal = ({ open, onClose, onAddProvider }) => {
  const [activeStep, setActiveStep] = useState(0);
  const [formData, setFormData] = useState({
    // Personal Information
    firstName: '',
    lastName: '',
    email: '',
    phone: '',
    
    // Professional Information
    specialization: '',
    licenseNumber: '',
    yearsExperience: '',
    
    // Clinic Address
    streetAddress: '',
    city: '',
    state: '',
    zipCode: ''
  });
  
  const [errors, setErrors] = useState({});
  const [isLoading, setIsLoading] = useState(false);

  // Validation functions
  const validateEmail = (email) => {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailRegex.test(email);
  };

  const validatePhone = (phone) => {
    const phoneRegex = /^[+]?[1-9][\d]{0,15}$/;
    return phoneRegex.test(phone.replace(/\s/g, ''));
  };

  const validateLicenseNumber = (license) => {
    const licenseRegex = /^[A-Z0-9]{6,12}$/;
    return licenseRegex.test(license);
  };

  const validateZipCode = (zip) => {
    const zipRegex = /^\d{5}(-\d{4})?$/;
    return zipRegex.test(zip);
  };

  const validateField = (name, value) => {
    switch (name) {
      case 'firstName':
      case 'lastName':
        if (!value) return `${name === 'firstName' ? 'First' : 'Last'} name is required`;
        if (value.length < 2) return `${name === 'firstName' ? 'First' : 'Last'} name must be at least 2 characters`;
        if (value.length > 50) return `${name === 'firstName' ? 'First' : 'Last'} name must be less than 50 characters`;
        return '';
      
      case 'email':
        if (!value) return 'Email is required';
        if (!validateEmail(value)) return 'Please enter a valid email address';
        return '';
      
      case 'phone':
        if (!value) return 'Phone number is required';
        if (!validatePhone(value)) return 'Please enter a valid phone number';
        return '';
      
      case 'specialization':
        if (!value) return 'Specialization is required';
        return '';
      
      case 'licenseNumber':
        if (!value) return 'License number is required';
        if (!validateLicenseNumber(value)) return 'License number must be 6-12 alphanumeric characters';
        return '';
      
      case 'yearsExperience':
        if (!value) return 'Years of experience is required';
        if (isNaN(value) || value < 0 || value > 50) return 'Please enter a valid number of years (0-50)';
        return '';
      
      case 'streetAddress':
        if (!value) return 'Street address is required';
        return '';
      
      case 'city':
        if (!value) return 'City is required';
        return '';
      
      case 'state':
        if (!value) return 'State is required';
        return '';
      
      case 'zipCode':
        if (!value) return 'ZIP code is required';
        if (!validateZipCode(value)) return 'Please enter a valid ZIP code';
        return '';
      
      default:
        return '';
    }
  };

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));

    // Real-time validation
    const error = validateField(name, value);
    setErrors(prev => ({
      ...prev,
      [name]: error
    }));
  };

  const validateStep = (step) => {
    const newErrors = {};
    
    switch (step) {
      case 0: // Personal Information
        ['firstName', 'lastName', 'email', 'phone'].forEach(field => {
          const error = validateField(field, formData[field]);
          if (error) newErrors[field] = error;
        });
        break;
      
      case 1: // Professional Details
        ['specialization', 'licenseNumber', 'yearsExperience'].forEach(field => {
          const error = validateField(field, formData[field]);
          if (error) newErrors[field] = error;
        });
        break;
      
      case 2: // Clinic Address
        ['streetAddress', 'city', 'state', 'zipCode'].forEach(field => {
          const error = validateField(field, formData[field]);
          if (error) newErrors[field] = error;
        });
        break;
    }
    
    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleNext = () => {
    if (validateStep(activeStep)) {
      setActiveStep(prev => prev + 1);
    }
  };

  const handleBack = () => {
    setActiveStep(prev => prev - 1);
  };

  const handleSubmit = async () => {
    if (validateStep(activeStep)) {
      setIsLoading(true);
      
      // Simulate API call
      setTimeout(() => {
        const newProvider = {
          id: Date.now(), // Simple ID generation
          ...formData
        };
        
        onAddProvider(newProvider);
        handleClose();
        setIsLoading(false);
      }, 1000);
    }
  };

  const handleClose = () => {
    setActiveStep(0);
    setFormData({
      firstName: '',
      lastName: '',
      email: '',
      phone: '',
      specialization: '',
      licenseNumber: '',
      yearsExperience: '',
      streetAddress: '',
      city: '',
      state: '',
      zipCode: ''
    });
    setErrors({});
    onClose();
  };

  const renderStepContent = (step) => {
    switch (step) {
      case 0:
        return (
          <Fade in={true} timeout={500}>
            <Grid container spacing={3}>
              <Grid item xs={12} sm={6}>
                <TextField
                  fullWidth
                  label="First Name"
                  name="firstName"
                  value={formData.firstName}
                  onChange={handleInputChange}
                  error={!!errors.firstName}
                  helperText={errors.firstName}
                  InputProps={{
                    startAdornment: (
                      <InputAdornment position="start">
                        <Person color="action" />
                      </InputAdornment>
                    ),
                  }}
                />
              </Grid>
              <Grid item xs={12} sm={6}>
                <TextField
                  fullWidth
                  label="Last Name"
                  name="lastName"
                  value={formData.lastName}
                  onChange={handleInputChange}
                  error={!!errors.lastName}
                  helperText={errors.lastName}
                />
              </Grid>
              <Grid item xs={12}>
                <TextField
                  fullWidth
                  label="Email Address"
                  name="email"
                  type="email"
                  value={formData.email}
                  onChange={handleInputChange}
                  error={!!errors.email}
                  helperText={errors.email}
                  InputProps={{
                    startAdornment: (
                      <InputAdornment position="start">
                        <Email color="action" />
                      </InputAdornment>
                    ),
                  }}
                />
              </Grid>
              <Grid item xs={12}>
                <TextField
                  fullWidth
                  label="Phone Number"
                  name="phone"
                  value={formData.phone}
                  onChange={handleInputChange}
                  error={!!errors.phone}
                  helperText={errors.phone}
                  InputProps={{
                    startAdornment: (
                      <InputAdornment position="start">
                        <Phone color="action" />
                      </InputAdornment>
                    ),
                  }}
                />
              </Grid>
            </Grid>
          </Fade>
        );

      case 1:
        return (
          <Fade in={true} timeout={500}>
            <Grid container spacing={3}>
              <Grid item xs={12}>
                <FormControl fullWidth error={!!errors.specialization}>
                  <InputLabel>Specialization</InputLabel>
                  <Select
                    name="specialization"
                    value={formData.specialization}
                    onChange={handleInputChange}
                    label="Specialization"
                    startAdornment={
                      <InputAdornment position="start">
                        <School color="action" />
                      </InputAdornment>
                    }
                  >
                    {specializations.map((spec) => (
                      <MenuItem key={spec} value={spec}>{spec}</MenuItem>
                    ))}
                  </Select>
                  {errors.specialization && (
                    <Typography variant="caption" color="error" sx={{ mt: 1, display: 'block' }}>
                      {errors.specialization}
                    </Typography>
                  )}
                </FormControl>
              </Grid>
              <Grid item xs={12} sm={6}>
                <TextField
                  fullWidth
                  label="License Number"
                  name="licenseNumber"
                  value={formData.licenseNumber}
                  onChange={handleInputChange}
                  error={!!errors.licenseNumber}
                  helperText={errors.licenseNumber}
                  InputProps={{
                    startAdornment: (
                      <InputAdornment position="start">
                        <Work color="action" />
                      </InputAdornment>
                    ),
                  }}
                />
              </Grid>
              <Grid item xs={12} sm={6}>
                <TextField
                  fullWidth
                  label="Years of Experience"
                  name="yearsExperience"
                  type="number"
                  value={formData.yearsExperience}
                  onChange={handleInputChange}
                  error={!!errors.yearsExperience}
                  helperText={errors.yearsExperience}
                  InputProps={{
                    startAdornment: (
                      <InputAdornment position="start">
                        <Business color="action" />
                      </InputAdornment>
                    ),
                  }}
                />
              </Grid>
            </Grid>
          </Fade>
        );

      case 2:
        return (
          <Fade in={true} timeout={500}>
            <Grid container spacing={3}>
              <Grid item xs={12}>
                <TextField
                  fullWidth
                  label="Street Address"
                  name="streetAddress"
                  value={formData.streetAddress}
                  onChange={handleInputChange}
                  error={!!errors.streetAddress}
                  helperText={errors.streetAddress}
                  InputProps={{
                    startAdornment: (
                      <InputAdornment position="start">
                        <Home color="action" />
                      </InputAdornment>
                    ),
                  }}
                />
              </Grid>
              <Grid item xs={12} sm={6}>
                <TextField
                  fullWidth
                  label="City"
                  name="city"
                  value={formData.city}
                  onChange={handleInputChange}
                  error={!!errors.city}
                  helperText={errors.city}
                  InputProps={{
                    startAdornment: (
                      <InputAdornment position="start">
                        <LocationOn color="action" />
                      </InputAdornment>
                    ),
                  }}
                />
              </Grid>
              <Grid item xs={12} sm={3}>
                <TextField
                  fullWidth
                  label="State"
                  name="state"
                  value={formData.state}
                  onChange={handleInputChange}
                  error={!!errors.state}
                  helperText={errors.state}
                />
              </Grid>
              <Grid item xs={12} sm={3}>
                <TextField
                  fullWidth
                  label="ZIP Code"
                  name="zipCode"
                  value={formData.zipCode}
                  onChange={handleInputChange}
                  error={!!errors.zipCode}
                  helperText={errors.zipCode}
                />
              </Grid>
            </Grid>
          </Fade>
        );

      default:
        return null;
    }
  };

  return (
    <Dialog 
      open={open} 
      onClose={handleClose}
      maxWidth="md"
      fullWidth
      PaperProps={{
        sx: {
          borderRadius: 3,
          overflow: 'hidden'
        }
      }}
    >
      <DialogTitle sx={{ 
        background: 'linear-gradient(45deg, #2e7d32, #4caf50)',
        color: 'white',
        display: 'flex',
        justifyContent: 'space-between',
        alignItems: 'center'
      }}>
        <Typography variant="h6" sx={{ fontWeight: 600 }}>
          Add New Provider
        </Typography>
        <IconButton onClick={handleClose} sx={{ color: 'white' }}>
          <Close />
        </IconButton>
      </DialogTitle>

      <DialogContent sx={{ p: 4 }}>
        <Stepper activeStep={activeStep} sx={{ mb: 4 }}>
          {steps.map((label) => (
            <Step key={label}>
              <StepLabel>{label}</StepLabel>
            </Step>
          ))}
        </Stepper>

        {renderStepContent(activeStep)}
      </DialogContent>

      <DialogActions sx={{ p: 3, gap: 2 }}>
        <Button
          disabled={activeStep === 0}
          onClick={handleBack}
          variant="outlined"
          sx={{
            borderColor: '#2e7d32',
            color: '#2e7d32',
            '&:hover': {
              borderColor: '#1b5e20',
              backgroundColor: 'rgba(46, 125, 50, 0.04)',
            }
          }}
        >
          Back
        </Button>
        
        <Box sx={{ flex: 1 }} />
        
        {activeStep === steps.length - 1 ? (
          <Button
            onClick={handleSubmit}
            variant="contained"
            disabled={isLoading}
            sx={{
              background: 'linear-gradient(45deg, #2e7d32, #4caf50)',
              '&:hover': {
                background: 'linear-gradient(45deg, #1b5e20, #2e7d32)',
              }
            }}
          >
            {isLoading ? <CircularProgress size={24} color="inherit" /> : 'Add Provider'}
          </Button>
        ) : (
          <Button
            onClick={handleNext}
            variant="contained"
            sx={{
              background: 'linear-gradient(45deg, #2e7d32, #4caf50)',
              '&:hover': {
                background: 'linear-gradient(45deg, #1b5e20, #2e7d32)',
              }
            }}
          >
            Next
          </Button>
        )}
      </DialogActions>
    </Dialog>
  );
};

export default AddProviderModal; 